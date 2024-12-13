package com.ctlfab.condomini.service;

import com.ctlfab.condomini.DTO.OutlayDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface OutlayService {

    List<OutlayDTO> findApartmentOutlaysByApartmentId(Long apartmentId);
    List<OutlayDTO> findAllOutlaysByCondominiumId(Long condominiumId);
    OutlayDTO saveOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id);
    OutlayDTO updateOutlay(OutlayDTO outlayDTO, Long condominiumId, Long apartmentId, Long id);
    Boolean deleteOutlay(Long id);
    Float totalAmountByApartmentId(Long apartmentId);
}
