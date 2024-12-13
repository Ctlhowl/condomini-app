package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.*;
import com.ctlfab.condomini.model.Condominium;
import com.ctlfab.condomini.model.Report;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.repository.ReportRepository;
import com.ctlfab.condomini.service.OutlayService;
import com.ctlfab.condomini.service.ReportService;
import com.ctlfab.condomini.service.TableAppendixService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final CondominiumRepository condominiumRepository;
    private final TableAppendixService tableAppendixService;
    private final OutlayService outlayService;

    @Override
    public ReportDTO saveReport(ReportDTO reportDTO) {
        logger.info("Saving report {}", reportDTO);

        Report report = mapDTOToEntity(reportDTO);
        return mapEntityToDTO(reportRepository.save(report));
    }

    @Override
    public ReportDTO findReportById(Long reportId) {
        logger.info("Fetching report by ID: {}", reportId);

        return mapEntityToDTO(reportRepository.findById(reportId).get());
    }

    @Override
    public Collection<ReportDTO> findAllReportByCondominiumId(Long condominiumId) {
        logger.info("Fetching all report by condominium ID: {}", condominiumId);

        Collection<ReportDTO> reports = new LinkedList<>();

        for(Report report : reportRepository.findAllReportByCondominiumId(condominiumId)) {
            reports.add(mapEntityToDTO(report));
        }

        return reports;
    }

    @Override
    public ByteArrayInputStream exportToPDF(CondominiumDTO condominiumDTO) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        String title = "Report Condominale del " + now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.addTitle(title);
            PdfName pdfName = new PdfName(title);
            document.setRole(pdfName);

            Paragraph paragraph = new Paragraph(title, font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            addQuoteTable(document, condominiumDTO);

            Rectangle layout = new Rectangle(PageSize.A4).rotate();
            document.setPageSize(layout);
            document.newPage();
            addCondominiumOutlay(document, condominiumDTO);

            document.newPage();
            addApartmentOutlay(document, condominiumDTO.getApartments());

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void addApartmentOutlay(Document document, List<ApartmentDTO> apartmentsDTO) throws DocumentException{
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Spese Appartamenti" ,titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        addApartmentOutlayTable(document, apartmentsDTO);
    }

    private void addApartmentOutlayTable(Document document, List<ApartmentDTO> apartmentsDTO) throws DocumentException{
        PdfPTable table = new PdfPTable(14);
        table.setWidthPercentage(100);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);


        Stream.of( "Proprietario", "Tabella A", "Tabella B", "Tabella C", "Tabella D", "Saldo Precedente", "Totale Spese", "Conguaglio Inizio Anno",  "Totale Versato", "Conguaglio Finale").forEach((headerTitle) -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            if(headerTitle.equals("Tabella A") || headerTitle.equals("Tabella B") ||
                headerTitle.equals("Tabella C") || headerTitle.equals("Tabella D")){
                header.setColspan(2);
            }

            header.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(header);
        });

        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        emptyCell.setPhrase(new Phrase("", headerFont));
        table.addCell(emptyCell);

        Stream.of("Mill A", "Mill B", "Mill C", "Mill D").forEach((headerTitle) -> {
            PdfPCell amountCell = new PdfPCell();
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            amountCell.setPhrase(new Phrase("Importo Dovuto", headerFont));
            table.addCell(amountCell);

            PdfPCell mill = new PdfPCell();
            mill.setHorizontalAlignment(Element.ALIGN_CENTER);
            mill.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mill.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(mill);
        });

        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(emptyCell);

        AtomicReference<Float> totalAmountTabA = new AtomicReference<>(0f);
        AtomicReference<Float> totalAmountTabB = new AtomicReference<>(0f);
        AtomicReference<Float> totalAmountTabC = new AtomicReference<>(0f);
        AtomicReference<Float> totalAmountTabD = new AtomicReference<>(0f);
        AtomicReference<Float> totalMillTabA = new AtomicReference<>(0f);
        AtomicReference<Float> totalMillTabB = new AtomicReference<>(0f);
        AtomicReference<Float> totalMillTabC = new AtomicReference<>(0f);
        AtomicReference<Float> totalMillTabD = new AtomicReference<>(0f);
        AtomicReference<Float> totalLastYearBalance = new AtomicReference<>(0f);
        AtomicReference<Float> totalAmount = new AtomicReference<>(0f);
        AtomicReference<Float> totalFirstBalance = new AtomicReference<>(0f);
        AtomicReference<Float> totalOutlay = new AtomicReference<>(0f);
        AtomicReference<Float> totalFinalBalance = new AtomicReference<>(0f);

        apartmentsDTO.forEach(apartment -> {
            PdfPCell owner = new PdfPCell(new Phrase(apartment.getOwner(), bodyFont));
            owner.setPadding(2);
            owner.setVerticalAlignment(Element.ALIGN_MIDDLE);
            owner.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(owner);

            Float totA = tableAppendixService.findTotalQuoteByCategory("A");
            PdfPCell amountTabA = new PdfPCell(new Phrase(totA.toString(), bodyFont));
            amountTabA.setPadding(2);
            amountTabA.setVerticalAlignment(Element.ALIGN_MIDDLE);
            amountTabA.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(amountTabA);

            PdfPCell millA = new PdfPCell(new Phrase(String.valueOf(apartment.getMillTabA()), bodyFont));
            millA.setPadding(2);
            millA.setVerticalAlignment(Element.ALIGN_MIDDLE);
            millA.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(millA);

            Float totB = tableAppendixService.findTotalQuoteByCategory("B");
            PdfPCell amountTabB = new PdfPCell(new Phrase(totB.toString(), bodyFont));
            amountTabB.setPadding(2);
            amountTabB.setVerticalAlignment(Element.ALIGN_MIDDLE);
            amountTabB.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(amountTabB);

            PdfPCell millB = new PdfPCell(new Phrase(String.valueOf(apartment.getMillTabB()), bodyFont));
            millB.setPadding(2);
            millB.setVerticalAlignment(Element.ALIGN_MIDDLE);
            millB.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(millB);

            Float totC = tableAppendixService.findTotalQuoteByCategory("C");
            PdfPCell amountTabC = new PdfPCell(new Phrase(totC.toString(), bodyFont));
            amountTabC.setPadding(2);
            amountTabC.setVerticalAlignment(Element.ALIGN_MIDDLE);
            amountTabC.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(amountTabC);

            PdfPCell millC = new PdfPCell(new Phrase(String.valueOf(apartment.getMillTabC()), bodyFont));
            millC.setPadding(2);
            millC.setVerticalAlignment(Element.ALIGN_MIDDLE);
            millC.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(millC);

            Float totD = tableAppendixService.findTotalQuoteByCategory("D");
            PdfPCell amountTabD = new PdfPCell(new Phrase(totD.toString(), bodyFont));
            amountTabD.setPadding(2);
            amountTabD.setVerticalAlignment(Element.ALIGN_MIDDLE);
            amountTabD.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(amountTabD);

            PdfPCell millD = new PdfPCell(new Phrase(String.valueOf(apartment.getMillTabD()), bodyFont));
            millD.setPadding(2);
            millD.setVerticalAlignment(Element.ALIGN_MIDDLE);
            millD.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(millD);

            String lastYearBalanceStr = apartment.getLastYearBalance() == 0 ? "0" : String.valueOf(apartment.getLastYearBalance());
            PdfPCell lastYearBalance = new PdfPCell(new Phrase(lastYearBalanceStr, bodyFont));
            lastYearBalance.setPadding(2);
            lastYearBalance.setVerticalAlignment(Element.ALIGN_MIDDLE);
            lastYearBalance.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(lastYearBalance);

            Float totalOutlayTab = totA + totB + totC + totD;
            PdfPCell totalOutlayCell = new PdfPCell(new Phrase(totalOutlayTab.toString(), bodyFont));
            totalOutlayCell.setPadding(2);
            totalOutlayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalOutlayCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(totalOutlayCell);

            Float firstBalance = apartment.getLastYearBalance() + totalOutlayTab;
            PdfPCell firstBalanceCell = new PdfPCell(new Phrase(firstBalance.toString(), bodyFont));
            firstBalanceCell.setPadding(2);
            firstBalanceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            firstBalanceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(firstBalanceCell);

            Float amount = outlayService.totalAmountByApartmentId(apartment.getId()) == null ? 0f : outlayService.totalAmountByApartmentId(apartment.getId());
            PdfPCell totalAmountCell = new PdfPCell(new Phrase(String.valueOf(amount), bodyFont));
            totalAmountCell.setPadding(2);
            totalAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalAmountCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(totalAmountCell);

            Float finalBalance = firstBalance + amount;
            PdfPCell finalBalanceCell = new PdfPCell(new Phrase(finalBalance.toString(), bodyFont));
            finalBalanceCell.setPadding(2);
            finalBalanceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            finalBalanceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(finalBalanceCell);

            totalAmountTabA.updateAndGet(v -> v + totA);
            totalAmountTabB.updateAndGet(v -> v + totB);
            totalAmountTabC.updateAndGet(v -> v + totC);
            totalAmountTabD.updateAndGet(v -> v + totD);
            totalMillTabA.updateAndGet(v -> v + apartment.getMillTabA());
            totalMillTabB.updateAndGet(v -> v + apartment.getMillTabB());
            totalMillTabC.updateAndGet(v -> v + apartment.getMillTabC());
            totalMillTabD.updateAndGet(v -> v + apartment.getMillTabD());
            totalLastYearBalance.updateAndGet(v -> v + apartment.getLastYearBalance());
            totalOutlay.updateAndGet(v -> v + totalOutlayTab);
            totalFirstBalance.updateAndGet(v -> v + firstBalance);
            totalAmount.updateAndGet(v -> v + amount);
            totalFinalBalance.updateAndGet(v -> v + finalBalance);
        });

        PdfPCell totalStr = new PdfPCell(new Phrase("Totale", headerFont));
        totalStr.setPadding(2);
        totalStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalStr);

        PdfPCell totalAmountTabAStr = new PdfPCell(new Phrase("€" + totalAmountTabA , headerFont));
        totalAmountTabAStr.setPadding(2);
        totalAmountTabAStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmountTabAStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalAmountTabAStr);

        PdfPCell totalMillTabAStr = new PdfPCell(new Phrase(totalMillTabA.toString() , headerFont));
        totalMillTabAStr.setPadding(2);
        totalMillTabAStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalMillTabAStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalMillTabAStr);

        PdfPCell totalAmountTabBStr = new PdfPCell(new Phrase("€" + totalAmountTabB , headerFont));
        totalAmountTabBStr.setPadding(2);
        totalAmountTabBStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmountTabBStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalAmountTabBStr);

        PdfPCell totalMillTabBStr = new PdfPCell(new Phrase(totalMillTabB.toString() , headerFont));
        totalMillTabBStr.setPadding(2);
        totalMillTabBStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalMillTabBStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalMillTabBStr);

        PdfPCell totalAmountTabCStr = new PdfPCell(new Phrase("€" + totalAmountTabC , headerFont));
        totalAmountTabCStr.setPadding(2);
        totalAmountTabCStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmountTabCStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalAmountTabCStr);

        PdfPCell totalMillTabCStr = new PdfPCell(new Phrase(totalMillTabC.toString() , headerFont));
        totalMillTabCStr.setPadding(2);
        totalMillTabCStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalMillTabCStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalMillTabCStr);

        PdfPCell totalAmountTabDStr = new PdfPCell(new Phrase("€" + totalAmountTabD , headerFont));
        totalAmountTabDStr.setPadding(2);
        totalAmountTabDStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmountTabDStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalAmountTabDStr);

        PdfPCell totalMillTabDStr = new PdfPCell(new Phrase(totalMillTabD.toString() , headerFont));
        totalMillTabDStr.setPadding(2);
        totalMillTabDStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalMillTabDStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalMillTabDStr);

        PdfPCell totalLastYearBalanceStr = new PdfPCell(new Phrase("€" + totalLastYearBalance , headerFont));
        totalLastYearBalanceStr.setPadding(2);
        totalLastYearBalanceStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalLastYearBalanceStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalLastYearBalanceStr);

        PdfPCell totalOutlayStr = new PdfPCell(new Phrase("€" + totalOutlay , headerFont));
        totalOutlayStr.setPadding(2);
        totalOutlayStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalOutlayStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalOutlayStr);

        PdfPCell totalFirstBalanceStr = new PdfPCell(new Phrase("€" + totalFirstBalance , headerFont));
        totalFirstBalanceStr.setPadding(2);
        totalFirstBalanceStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalFirstBalanceStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalFirstBalanceStr);

        PdfPCell totalAmountStr = new PdfPCell(new Phrase("€" + totalAmount , headerFont));
        totalAmountStr.setPadding(2);
        totalAmountStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmountStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalAmountStr);

        PdfPCell totalFinalBalanceStr = new PdfPCell(new Phrase("€" + totalFinalBalance , headerFont));
        totalFinalBalanceStr.setPadding(2);
        totalFinalBalanceStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalFinalBalanceStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(totalFinalBalanceStr);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addCondominiumOutlay(Document document, CondominiumDTO condominiumDTO) throws DocumentException{
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Spese Condominali" ,titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        addCondominiumOutlayTable(document, condominiumDTO.getOutlays());
    }

    private void addCondominiumOutlayTable(Document document, List<OutlayDTO> outlayDTOs) throws DocumentException {
        PdfPTable table = new PdfPTable(7);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);


        Stream.of( "Data", "Descrizione", "Importo", "Operazione", "Pagamento", "Spesa", "Tabella").forEach((headerTitle) -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            header.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(header);
        });

        outlayDTOs.forEach(outlay -> {
            Timestamp timestamp = outlay.getCreated_at();
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            String dateStr = localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            PdfPCell date = new PdfPCell(new Phrase(dateStr, bodyFont));
            date.setPadding(2);
            date.setVerticalAlignment(Element.ALIGN_MIDDLE);
            date.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(date);

            PdfPCell description = new PdfPCell(new Phrase(outlay.getDescription(), bodyFont));
            description.setPadding(2);
            description.setVerticalAlignment(Element.ALIGN_MIDDLE);
            description.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(description);

            String amountStr = outlay.getAmount() == 0 ? "0" : String.valueOf(outlay.getAmount());
            PdfPCell amount = new PdfPCell(new Phrase("€ " + amountStr, bodyFont));
            amount.setPadding(2);
            amount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            amount.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(amount);

            PdfPCell operation = new PdfPCell(new Phrase(String.valueOf(outlay.getOperationType()), bodyFont));
            operation.setPadding(2);
            operation.setVerticalAlignment(Element.ALIGN_MIDDLE);
            operation.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(operation);

            PdfPCell payment = new PdfPCell(new Phrase(String.valueOf(outlay.getPaymentMethod()), bodyFont));
            payment.setPadding(2);
            payment.setVerticalAlignment(Element.ALIGN_MIDDLE);
            payment.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(payment);

            PdfPCell outlayCell = new PdfPCell(new Phrase(String.valueOf(outlay.getOutlayType()), bodyFont));
            outlayCell.setPadding(2);
            outlayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            outlayCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(outlayCell);

            String tableStr = outlay.getTable().getCategory() + " - " + outlay.getTable().getDescription();
            PdfPCell tableCell = new PdfPCell(new Phrase(tableStr, bodyFont));
            description.setPadding(2);
            description.setVerticalAlignment(Element.ALIGN_MIDDLE);
            description.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(tableCell);
        });


        PdfPCell totalStr = new PdfPCell(new Phrase("Totale", headerFont));
        totalStr.setPadding(2);
        totalStr.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalStr.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalStr.setColspan(2);
        table.addCell(totalStr);

        Float total = 0f;
        for(OutlayDTO outlayDTO : outlayDTOs) {
            total += outlayDTO.getAmount();
        }
        PdfPCell totalAmount = new PdfPCell(new Phrase("€ " + total, headerFont));
        totalAmount.setPadding(2);
        totalAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
        totalAmount.setHorizontalAlignment(Element.ALIGN_LEFT);
        totalAmount.setColspan(5);
        table.addCell(totalAmount);

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addQuoteTable(Document document, CondominiumDTO condominiumDTO) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Preventivi" ,titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        List<QuoteDTO> quoteTabA = condominiumDTO.getQuotes()
                .stream()
                .filter(quote -> quote.getTable().getCategory().equals("A"))
                .toList();
        addTableQuote("Tabella A", document, quoteTabA);

        List<QuoteDTO> quoteTabB = condominiumDTO.getQuotes()
                .stream()
                .filter(quote -> quote.getTable().getCategory().equals("B"))
                .toList();
        addTableQuote("Tabella B", document, quoteTabB);

        List<QuoteDTO> quoteTabC = condominiumDTO.getQuotes()
                .stream()
                .filter(quote -> quote.getTable().getCategory().equals("C"))
                .toList();
        addTableQuote("Tabella C", document, quoteTabC);

        List<QuoteDTO> quoteTabD = condominiumDTO.getQuotes()
                .stream()
                .filter(quote -> quote.getTable().getCategory().equals("D"))
                .toList();
        addTableQuote("Tabella D", document, quoteTabD);
    }

    private void addTableQuote(String tableTitle, Document document, List<QuoteDTO> quotes) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        float[] widths = new float[] {2f, 1f };
        table.setWidths(widths);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        PdfPCell titleCell = new PdfPCell(new Phrase(tableTitle, headerFont));
        titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        titleCell.setColspan(2);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(titleCell);

        Stream.of( "Descrizione", "Preventivo").forEach((headerTitle) -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            header.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(header);
        });

        quotes.forEach( quote -> {
            PdfPCell description = new PdfPCell(new Phrase(quote.getTable().getDescription(), bodyFont));
            description.setPadding(2);
            description.setVerticalAlignment(Element.ALIGN_MIDDLE);
            description.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(description);

            String totalAmountStr = quote.getTotalAmount() == 0 ? "0" : String.valueOf(quote.getTotalAmount());
            PdfPCell totalAmount = new PdfPCell(new Phrase(totalAmountStr, bodyFont));
            totalAmount.setPadding(2);
            totalAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(totalAmount);
        });

        document.add(table);
        document.add(Chunk.NEWLINE);
    }
    private Report mapDTOToEntity(ReportDTO reportDTO) {
        Condominium condominium = condominiumRepository.findCondominiumByReportId(reportDTO.getId());

        return Report.builder()
                .id(reportDTO.getId())
                .path_file(reportDTO.getPathFile())
                .created_at(reportDTO.getCreated_at())
                .condominium(condominium)
                .build();
    }

    private ReportDTO mapEntityToDTO(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .pathFile(report.getPath_file())
                .created_at(report.getCreated_at())
                .build();
    }
}
