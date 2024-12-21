package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.dto.OutlayDTO;
import com.ctlfab.condomini.dto.TableAppendixDTO;
import com.ctlfab.condomini.model.Apartment;
import com.ctlfab.condomini.model.Condominium;
import com.ctlfab.condomini.model.Outlay;
import com.ctlfab.condomini.model.TableAppendix;
import com.ctlfab.condomini.repository.ApartmentRepository;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.repository.OutlayRepository;
import com.ctlfab.condomini.repository.TableAppendixRepository;
import com.ctlfab.condomini.service.MyUtils;
import com.ctlfab.condomini.service.OutlayService;
import com.ctlfab.condomini.service.TableAppendixService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class OutlayServiceImpl implements OutlayService {
    private final OutlayRepository outlayRepository;
    private static final Logger logger = LoggerFactory.getLogger(OutlayServiceImpl.class);
    private final TableAppendixService tableAppendixService;
    private final TableAppendixRepository tableAppendixRepository;
    private final ApartmentRepository apartmentRepository;
    private final CondominiumRepository condominiumRepository;
    private final MyUtils myUtils;

    @Override
    public List<OutlayDTO> findApartmentOutlaysByApartmentId(Long apartmentId) {
        logger.info("Fetching apartment outlays by apartment ID: {}", apartmentId);
        List<OutlayDTO> outlays = new LinkedList<>();

        for(Outlay outlay : outlayRepository.findApartmentOutlaysByApartmentId(apartmentId, myUtils.startDateTime(), myUtils.endDateTime())) {
            outlays.add(mapEntityToDTO(outlay));
        }

        return outlays;
    }

    @Override
    public List<OutlayDTO> findApartmentOutlaysByApartmentIdAndYear(Long apartmentId, int year) {
        logger.info("Fetching apartment outlays {} by apartment ID: {}", year, apartmentId);
        List<OutlayDTO> outlays = new LinkedList<>();

        for(Outlay outlay : outlayRepository.findApartmentOutlaysByApartmentId(apartmentId, myUtils.startDateTime(year), myUtils.endDateTime(year))) {
            outlays.add(mapEntityToDTO(outlay));
        }

        return outlays;
    }


    @Override
    public List<OutlayDTO> findAllOutlaysByCondominiumId(Long condominiumId) {
        logger.info("Fetching all outlays by condominium ID: {}", condominiumId);
        List<OutlayDTO> outlays = new LinkedList<>();

        for(Outlay outlay : outlayRepository.findCondominiumOutlaysByCondominiumId(condominiumId, myUtils.startDateTime(), myUtils.endDateTime())){
            outlays.add(mapEntityToDTO(outlay));
        }

        return outlays;
    }


    @Override
    public List<OutlayDTO> findAllOutlaysByCondominiumIdAndYear(Long condominiumId, int year) {
        logger.info("Fetching all outlays {} by condominium ID: {}", year, condominiumId);
        List<OutlayDTO> outlays = new LinkedList<>();

        for(Outlay outlay : outlayRepository.findCondominiumOutlaysByCondominiumId(condominiumId, myUtils.startDateTime(year), myUtils.endDateTime(year))){
            outlays.add(mapEntityToDTO(outlay));
        }

        return outlays;
    }

    @Override
    public OutlayDTO saveOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id) {
        logger.info("Saving outlay: {}", outlayDTO);

        Outlay outlay = mapDTOToEntity(outlayDTO, condominiumId, apartmentId);
        return mapEntityToDTO(outlayRepository.save(outlay));
    }

    @Override
    public OutlayDTO updateOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id) {
        logger.info("Updating outlay: {}", outlayDTO);

        Outlay outlay = mapDTOToEntity(outlayDTO, condominiumId, apartmentId);
        return mapEntityToDTO(outlayRepository.save(outlay));
    }

    @Override
    public Boolean deleteOutlay(Long id) {
        logger.info("Deleting outlay: {}", id);

        outlayRepository.deleteById(id);
        return TRUE;
    }

    @Override
    public Float totalAmountByApartmentId(Long apartmentId, int year) {
        logger.info("Fetching total amount of outlay {} by Apartment Id: {} ", year, apartmentId);
        return outlayRepository.totalAmountByApartmentId(apartmentId, myUtils.startDateTime(year), myUtils.endDateTime(year));
    }

    private OutlayDTO mapEntityToDTO(Outlay outlay) {
        TableAppendixDTO tableDTO = tableAppendixService.findTableById(outlay.getTable().getId());

        return OutlayDTO.builder()
                .id(outlay.getId())
                .amount(outlay.getAmount())
                .createdAt(outlay.getCreatedAt())
                .description(outlay.getDescription())
                .operationType(outlay.getOperationType())
                .outlayType(outlay.getOutlayType())
                .paymentMethod(outlay.getPaymentMethod())
                .table(tableDTO)
                .build();
    }

    private Outlay mapDTOToEntity(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId) {
        TableAppendix table = tableAppendixRepository.findById(outlayDTO.getTable().getId()).get();
        Condominium condominium = condominiumRepository.findById(condominiumId).get();
        Apartment apartment = apartmentId != null ? apartmentRepository.findById(apartmentId).get() : null;

        return Outlay.builder()
                .id(outlayDTO.getId())
                .amount(outlayDTO.getAmount())
                .createdAt(outlayDTO.getCreatedAt())
                .description(outlayDTO.getDescription())
                .operationType(outlayDTO.getOperationType())
                .outlayType(outlayDTO.getOutlayType())
                .paymentMethod(outlayDTO.getPaymentMethod())
                .table(table)
                .apartment(apartment)
                .condominium(condominium)
                .build();
    }
}
