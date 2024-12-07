package com.ctlfab.condomini.service;

import com.ctlfab.condomini.DTO.CondominiumDTO;

import java.util.Collection;

public interface CondominiumService {
    CondominiumDTO saveCondominium(CondominiumDTO condominiumDTO);
    CondominiumDTO updateCondominium(CondominiumDTO condominiumDTO);
    Boolean deleteCondominium(Long id);
    CondominiumDTO findCondominiumById(Long id);
    Collection<CondominiumDTO> findAllCondominiums();
}
