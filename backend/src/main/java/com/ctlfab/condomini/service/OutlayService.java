package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.OutlayDTO;

import java.util.List;

public interface OutlayService {

    List<OutlayDTO> findApartmentOutlaysByApartmentId(Long apartmentId);
    List<OutlayDTO> findApartmentOutlaysByApartmentIdAndYear(Long apartmentId, int year);


    List<OutlayDTO> findAllOutlaysByCondominiumId(Long condominiumId);
    List<OutlayDTO> findAllOutlaysByCondominiumIdAndYear(Long condominiumId, int year);

    OutlayDTO saveOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id);
    OutlayDTO updateOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id);
    Boolean deleteOutlay(Long id);

    Float totalAmountByApartmentId(Long apartmentId, int year);
}
