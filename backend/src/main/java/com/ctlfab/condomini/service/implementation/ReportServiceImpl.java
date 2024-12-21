package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.dto.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final TableAppendixService tableAppendixService;
    private final OutlayService outlayService;

    @Override
    public ByteArrayInputStream exportToPDF(CondominiumDTO condominiumDTO, int year) {
        logger.info("Creating Report");

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addFirstPage(document, year);
            addQuoteTablePage(document, condominiumDTO);
            document.newPage();

            // Change landscape
            Rectangle layout = new Rectangle(PageSize.A4).rotate();
            document.setPageSize(layout);

            addCondominiumOutlay(document, condominiumDTO);
            document.newPage();

            addApartmentsOutlayPage(document, condominiumDTO.getApartments(), year);
            document.newPage();

            for(ApartmentDTO apartmentDTO : condominiumDTO.getApartments()) {
                addApartmentOutlayDetailsPage(document, apartmentDTO);
                document.newPage();
            }

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private void addFirstPage(Document document, int year) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        String title = "Bilancio Esercizio " + year + " - " + (year + 1);
        PdfName pdfName = new PdfName(title);

        document.setRole(pdfName);
        document.addTitle(title);

        Paragraph paragraph = new Paragraph(title, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
    }

    private void addQuoteTablePage(Document document, CondominiumDTO condominiumDTO) throws DocumentException {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Preventivi" ,font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);


        // Retrieve quote by table category
        List<QuoteDTO> quoteTabA = condominiumDTO.getQuotes()
                                                 .stream()
                                                 .filter(quote -> quote.getTable().getCategory().equals("A"))
                                                 .toList();

        List<QuoteDTO> quoteTabB = condominiumDTO.getQuotes()
                                                 .stream()
                                                 .filter(quote -> quote.getTable().getCategory().equals("B"))
                                                 .toList();

        List<QuoteDTO> quoteTabC = condominiumDTO.getQuotes()
                                                 .stream()
                                                 .filter(quote -> quote.getTable().getCategory().equals("C"))
                                                 .toList();
        List<QuoteDTO> quoteTabD = condominiumDTO.getQuotes()
                                                 .stream()
                                                 .filter(quote -> quote.getTable().getCategory().equals("D"))
                                                 .toList();

        // Set page
        final String tileTableA = "Table A";
        final String tileTableB = "Table B";
        final String tileTableC = "Table C";
        final String tileTableD = "Table D";

        addTableQuote(tileTableA, document, quoteTabA);
        addTableQuote(tileTableB, document, quoteTabB);
        addTableQuote(tileTableC, document, quoteTabC);
        addTableQuote(tileTableD, document, quoteTabD);
    }

    private void addApartmentsOutlayPage(Document document, List<ApartmentDTO> apartmentsDTO, int year) throws DocumentException{
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Spese Generali Appartamenti" ,font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        addApartmentOutlayTable(document, apartmentsDTO, year);
    }

    private void addApartmentOutlayDetailsPage(Document document, ApartmentDTO apartmentDTO) throws DocumentException{
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Paragraph title = new Paragraph("Spese Dettagliate Appartamento di " + apartmentDTO.getOwner() ,font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        addApartmentOutlayDetailsTable(document, apartmentDTO.getOutlays());
    }

    private void addApartmentOutlayDetailsTable(Document document, List<OutlayDTO> outlaysDTO) throws DocumentException {
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        // Set Title of header
        List<String> tableHead = new ArrayList<>();
        tableHead.add("Data");
        tableHead.add("Descrizione");
        tableHead.add("Importo");
        tableHead.add("Tipo Operazione");
        tableHead.add("Tipo Pagamento");
        tableHead.add("Tipo Spesa");
        tableHead.add("Tabella");

        tableHead.forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            header.setPhrase(new Phrase(headerTitle, headFont));
            table.addCell(header);
        });

        for(OutlayDTO outlayDTO : outlaysDTO){
            String date = outlayDTO.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            addCell(table, date);
            addCell(table, outlayDTO.getDescription());
            addCell(table, String.valueOf(outlayDTO.getAmount()));
            addCell(table, String.valueOf(outlayDTO.getOperationType()));
            addCell(table, String.valueOf(outlayDTO.getPaymentMethod()));
            addCell(table, String.valueOf(outlayDTO.getOutlayType()));

            String tableAppendix = outlayDTO.getTable().getCategory() + " - " + outlayDTO.getTable().getDescription();
            addCell(table, tableAppendix);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void addApartmentTableHead(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);

        // Set Title of header
        List<String> tableHead = new ArrayList<>();
        tableHead.add("Proprietario");
        tableHead.add("Tabella A");
        tableHead.add("Tabella B");
        tableHead.add("Tabella C");
        tableHead.add("Tabella D");
        tableHead.add("Saldo Precedente");
        tableHead.add("Totale Spese");
        tableHead.add("Conguaglio Inizio Anno");
        tableHead.add("Totale Versato");
        tableHead.add("Conguaglio Finale");

        tableHead.forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            if(headerTitle.equals("Tabella A") || headerTitle.equals("Tabella B") ||
                    headerTitle.equals("Tabella C") || headerTitle.equals("Tabella D")){
                header.setColspan(2);
            }

            header.setPhrase(new Phrase(headerTitle, font));
            table.addCell(header);
        });

        addSubApartmentTableHead(table);
    }

    private void addSubApartmentTableHead(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);

        // Set Title of sub header
        List<String> subTableHead = new ArrayList<>();
        subTableHead.add("Mill A");
        subTableHead.add("Mill B");
        subTableHead.add("Mill C");
        subTableHead.add("Mill D");

        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emptyCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        emptyCell.setPhrase(new Phrase(""));

        table.addCell(emptyCell);

        subTableHead.forEach(headerTitle -> {
            PdfPCell amountCell = new PdfPCell();
            amountCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            amountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            amountCell.setPhrase(new Phrase("Importo Dovuto", font));
            table.addCell(amountCell);

            PdfPCell mill = new PdfPCell();
            mill.setHorizontalAlignment(Element.ALIGN_CENTER);
            mill.setBackgroundColor(BaseColor.LIGHT_GRAY);
            mill.setPhrase(new Phrase(headerTitle, font));
            table.addCell(mill);
        });

        for(int i = 0; i < 5; i++){
            table.addCell(emptyCell);
        }
    }

    private void addCell(PdfPTable table, String data) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 6, BaseColor.BLACK);

        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setPadding(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String data, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setPadding(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    private void addApartmentOutlayTable(Document document, List<ApartmentDTO> apartmentsDTO, int year) throws DocumentException{
        PdfPTable table = new PdfPTable(14);
        table.setWidthPercentage(100);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
        addApartmentTableHead(table);

        float totalAmountTabA = 0f;
        float totalAmountTabB = 0f;
        float totalAmountTabC = 0f;
        float totalAmountTabD = 0f;
        float totalMillTabA = 0f;
        float totalMillTabB = 0f;
        float totalMillTabC = 0f;
        float totalMillTabD = 0f;
        float totalLastYearBalance = 0f;
        float totalAmount = 0f;
        float totalFirstBalance = 0f;
        float totalOutlay = 0f;
        float totalFinalBalance = 0f;

        for(ApartmentDTO apartmentDTO : apartmentsDTO){
            addCell(table, apartmentDTO.getOwner());

            Float totA = tableAppendixService.findTotalQuoteByCategoryAndYear("A", year);
            totA = totA == null ? 0 : totA;
            addCell(table, totA.toString());
            addCell(table, String.valueOf(apartmentDTO.getMillTabA()));

            Float totB = tableAppendixService.findTotalQuoteByCategoryAndYear("B", year);
            totB = totB == null ? 0 : totB;
            addCell(table, totB.toString());
            addCell(table, String.valueOf(apartmentDTO.getMillTabB()));

            Float totC = tableAppendixService.findTotalQuoteByCategoryAndYear("C", year);
            totC = totC == null ? 0 : totC;
            addCell(table, totC.toString());
            addCell(table, String.valueOf(apartmentDTO.getMillTabC()));

            Float totD = tableAppendixService.findTotalQuoteByCategoryAndYear("D", year);
            totD = totD == null ? 0 : totD;
            addCell(table, totD.toString());
            addCell(table, String.valueOf(apartmentDTO.getMillTabD()));

            String lastYearBalanceStr = apartmentDTO.getLastYearBalance() == 0 ? "0" : String.valueOf(apartmentDTO.getLastYearBalance());
            addCell(table, lastYearBalanceStr);

            Float totalTabOutlay = totA + totB + totC + totD;
            addCell(table, String.valueOf(totalTabOutlay));

            Float firstBalance = apartmentDTO.getLastYearBalance() + totalTabOutlay;
            addCell(table, String.valueOf(firstBalance));

            Float amount = outlayService.totalAmountByApartmentId(apartmentDTO.getId(), year);
            amount = amount == null ? 0 : amount;
            addCell(table, String.valueOf(amount));

            Float finalBalance = firstBalance + amount;
            addCell(table, String.valueOf(finalBalance));

            totalAmountTabA += totA;
            totalAmountTabB += totB;
            totalAmountTabC += totC;
            totalAmountTabD += totD;

            totalMillTabA += apartmentDTO.getMillTabA();
            totalMillTabB += apartmentDTO.getMillTabB();
            totalMillTabC += apartmentDTO.getMillTabC();
            totalMillTabD += apartmentDTO.getMillTabD();

            totalLastYearBalance += apartmentDTO.getLastYearBalance();
            totalOutlay += totalTabOutlay;
            totalFirstBalance += firstBalance;
            totalAmount += amount;
            totalFinalBalance += finalBalance;
        }

        String totalCell = "Totale";
        addCell(table, totalCell);

        addCell(table, "€" + totalAmountTabA, headerFont);
        addCell(table, String.valueOf(totalMillTabA), headerFont);

        addCell(table, "€" + totalAmountTabB, headerFont);
        addCell(table, String.valueOf(totalMillTabB), headerFont);

        addCell(table, "€" + totalAmountTabC, headerFont);
        addCell(table, String.valueOf(totalMillTabC), headerFont);

        addCell(table, "€" + totalAmountTabD, headerFont);
        addCell(table, String.valueOf(totalMillTabD), headerFont);

        addCell(table, "€" + totalLastYearBalance, headerFont);
        addCell(table, "€" + totalOutlay, headerFont);
        addCell(table, "€" + totalFirstBalance, headerFont);
        addCell(table, "€" + totalAmount, headerFont);
        addCell(table, "€" + totalFinalBalance, headerFont);

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


        Stream.of( "Data", "Descrizione", "Importo", "Operazione", "Pagamento", "Spesa", "Tabella").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            header.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(header);
        });

        outlayDTOs.forEach(outlay -> {
            Timestamp timestamp = outlay.getCreatedAt();
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

        float total = 0f;
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

    private void addTableQuote(String tableTitle, Document document, List<QuoteDTO> quotes) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        float[] widths = new float[] {2f, 1f };
        table.setWidths(widths);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

        PdfPCell titleCell = new PdfPCell(new Phrase(tableTitle, headerFont));
        titleCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setColspan(2);
        table.addCell(titleCell);

        Stream.of( "Descrizione", "Preventivo").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);

            header.setPhrase(new Phrase(headerTitle, headerFont));
            table.addCell(header);
        });

        for(QuoteDTO quoteDTO : quotes) {
            addCell(table, quoteDTO.getTable().getDescription(), bodyFont);

            String totalAmountStr = quoteDTO.getTotalAmount() == 0 ? "0" : String.valueOf(quoteDTO.getTotalAmount());
            addCell(table, totalAmountStr, bodyFont);
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }


}
