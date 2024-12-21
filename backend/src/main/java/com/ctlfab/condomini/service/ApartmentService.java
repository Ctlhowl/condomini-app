package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.ApartmentDTO;

import java.util.Collection;

public interface ApartmentService {
    ApartmentDTO saveApartment(ApartmentDTO apartmentDTO, Long condominiumId);
    ApartmentDTO updateApartment(ApartmentDTO apartmentDTO, Long condominiumId);
    Boolean deleteApartment(Long id);
    Collection<ApartmentDTO> findApartmentsByCondominiumId(Long condominiumId);
    Collection<ApartmentDTO> findApartmentsByCondominiumIdAndYear(Long condominiumId, int year);
}
