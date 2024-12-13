package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.*;
import com.ctlfab.condomini.model.*;
import com.ctlfab.condomini.repository.*;
import com.ctlfab.condomini.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class CondominiumServiceImpl implements CondominiumService {
    private final CondominiumRepository condominiumRepository;
    private static final Logger logger = LoggerFactory.getLogger(CondominiumServiceImpl.class);
    private final ApartmentRepository apartmentRepository;
    private final ApartmentService apartmentService;
    private final QuoteService quoteService;
    private final OutlayService outlayService;
    private final ReportService reportService;
    private final OutlayRepository outlayRepository;
    private final ReportRepository reportRepository;
    private final QuoteRepository quoteRepository;


    @Override
    public CondominiumDTO saveCondominium(CondominiumDTO condominiumDTO) {
        logger.info("Saving new condominium {}", condominiumDTO);

        Condominium condominium = mapDTOToEntity(condominiumDTO);

        return mapEntityToDTO(condominiumRepository.save(condominium));
    }

    @Override
    public CondominiumDTO updateCondominium(CondominiumDTO condominiumDTO) {
        logger.info("Updating condominium {}", condominiumDTO);

        Condominium condominium = mapDTOToEntity(condominiumDTO);

        return mapEntityToDTO(condominiumRepository.save(condominium));
    }

    @Override
    public Boolean deleteCondominium(Long id) {
        logger.info("Deleting condominium by ID: {}", id);
        condominiumRepository.deleteById(id);

        return TRUE;
    }

    @Override
    public Collection<CondominiumDTO> findAllCondominiums() {
        logger.info("Fetching all condominiums");

        Collection<CondominiumDTO> condominiums = new LinkedList<>();
        for(Condominium condominium : condominiumRepository.findAllOrderById()){
            condominiums.add(mapEntityToDTO(condominium));
        }

        return condominiums;
    }

    @Override
    public CondominiumDTO findCondominiumById(Long id) {
        logger.info("Fetching condominium by ID: {}", id);

        Condominium condominium = condominiumRepository.findById(id).get();

        return mapEntityToDTO(condominium);
    }

    private Condominium mapDTOToEntity(CondominiumDTO condominiumDTO){
        List<Outlay> outlays = outlayRepository.findCondominiumOutlaysByCondominiumId(condominiumDTO.getId()).stream().toList();


        Condominium condominium = Condominium.builder()
                .id(condominiumDTO.getId())
                .name(condominiumDTO.getName())
                .address(condominiumDTO.getAddress())
                .lastYearBalance(condominiumDTO.getLastYearBalance())
                .outlays(outlays)
                .build();

        if(condominiumDTO.getId() != null){
            List<Apartment> apartments = apartmentRepository.findApartmentsByCondominiumId(condominiumDTO.getId()).stream().toList();
            List<Report> reports = reportRepository.findAllReportByCondominiumId(condominiumDTO.getId()).stream().toList();
            List<Quote> quotes = quoteRepository.findAllQuotesByCondominium(condominiumDTO.getId()).stream().toList();

            condominium.setApartments(apartments);
            condominium.setReports(reports);
            condominium.setQuotes(quotes);
        }

        return condominium;
    }

    private CondominiumDTO mapEntityToDTO(Condominium condominium){
        List<OutlayDTO> outlaysDTO = outlayService.findAllOutlaysByCondominiumId(condominium.getId());
        List<ApartmentDTO> apartmentsDTO = apartmentService.findApartmentsByCondominiumId(condominium.getId()).stream().toList();
        List<ReportDTO> reportsDTO = reportService.findAllReportByCondominiumId(condominium.getId()).stream().toList();
        List<QuoteDTO> quotesDTO = quoteService.findAllQuotesByCondominium(condominium.getId()).stream().toList();

        return CondominiumDTO.builder()
                .id(condominium.getId())
                .name(condominium.getName())
                .address(condominium.getAddress())
                .lastYearBalance(condominium.getLastYearBalance())
                .outlays(outlaysDTO)
                .apartments(apartmentsDTO)
                .reports(reportsDTO)
                .quotes(quotesDTO)
                .build();
    }

}
