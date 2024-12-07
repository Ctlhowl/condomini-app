package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.ApartmentDTO;
import com.ctlfab.condomini.DTO.OutlayDTO;
import com.ctlfab.condomini.model.Apartment;
import com.ctlfab.condomini.model.Condominium;
import com.ctlfab.condomini.model.Outlay;
import com.ctlfab.condomini.repository.ApartmentRepository;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.repository.OutlayRepository;
import com.ctlfab.condomini.service.ApartmentService;
import com.ctlfab.condomini.service.OutlayService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.TRUE;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Transactional
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(ApartmentServiceImpl.class);
    private final CondominiumRepository condominiumRepository;
    private final OutlayRepository outlayRepository;
    private final OutlayService outlayService;

    @Override
    public ApartmentDTO saveApartment(ApartmentDTO apartmentDTO, Long condominiumId) {
        logger.info("Saving apartment {}", apartmentDTO);

        Apartment apartment = mapDTOToEntity(apartmentDTO, condominiumId);
        return mapEntityToDTO(apartmentRepository.save(apartment));
    }

    @Override
    public ApartmentDTO updateApartment(ApartmentDTO apartmentDTO, Long condominiumId) {
        logger.info("Updating apartment {}", apartmentDTO);

        Apartment apartment = mapDTOToEntity(apartmentDTO, condominiumId);
        return mapEntityToDTO(apartmentRepository.save(apartment));
    }

    @Override
    public Boolean deleteApartment(Long id) {
        logger.info("Deleting apartment {}", id);

        apartmentRepository.deleteById(id);
        return TRUE;
    }

    @Override
    public ApartmentDTO findApartmentById(Long id) {
        logger.info("Fetching apartment by ID: {} ", id);
        return mapEntityToDTO(apartmentRepository.findById(id).get());
    }

    @Override
    public Collection<ApartmentDTO> findApartmentsByCondominiumId(Long condominiumId) {
        logger.info("Fetching apartments by condominium ID: {}", condominiumId);

        Collection<ApartmentDTO> apartments = new LinkedList<>();
        for (Apartment apartment : apartmentRepository.findApartmentsByCondominiumId(condominiumId)) {
            apartments.add(mapEntityToDTO(apartment));
        }

        return apartments;
    }

    private ApartmentDTO mapEntityToDTO(Apartment apartment) {
        List<OutlayDTO> outlays = outlayService.findApartmentOutlaysByApartmentId(apartment.getId());

        return ApartmentDTO.builder()
                .id(apartment.getId())
                .owner(apartment.getOwner())
                .tenant(apartment.getTenant())
                .millTabA(apartment.getMillTabA())
                .millTabB(apartment.getMillTabB())
                .millTabC(apartment.getMillTabC())
                .millTabD(apartment.getMillTabD())
                .lastYearBalance(apartment.getLastYearBalance())
                .scala(apartment.getScala())
                .outlays(outlays)
                .build();
    }

    private Apartment mapDTOToEntity(ApartmentDTO apartmentDTO, Long condominiumId) {
        Optional<Condominium> condominium = condominiumRepository.findById(condominiumId);
        List<Outlay> outlays = outlayRepository.findAllOutlaysByTableId(apartmentDTO.getId()).stream().toList();

        return Apartment.builder()
                .id(apartmentDTO.getId())
                .owner(apartmentDTO.getOwner())
                .tenant(apartmentDTO.getTenant())
                .millTabA(apartmentDTO.getMillTabA())
                .millTabB(apartmentDTO.getMillTabB())
                .millTabC(apartmentDTO.getMillTabC())
                .millTabD(apartmentDTO.getMillTabD())
                .lastYearBalance(apartmentDTO.getLastYearBalance())
                .scala(apartmentDTO.getScala())
                .outlays(outlays)
                .condominium(condominium.orElse(null))
                .build();
    }
}