package com.ctlfab.condomini.service;

import com.ctlfab.condomini.DTO.TableAppendixDTO;

import java.util.Collection;

public interface TableAppendixService {
    TableAppendixDTO findTableById(Long tableId);
    Collection<TableAppendixDTO> findAll();
}
