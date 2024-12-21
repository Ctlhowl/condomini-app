package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.CondominiumDTO;

import java.util.Collection;

public interface CondominiumService {
    CondominiumDTO saveCondominium(CondominiumDTO condominiumDTO);
    CondominiumDTO updateCondominium(CondominiumDTO condominiumDTO);
    Boolean deleteCondominium(Long id);
    CondominiumDTO findCondominiumById(Long id);
    CondominiumDTO findCondominiumByIdAndYear(Long id, int year);
    Collection<CondominiumDTO> findAllCondominiums();
}
