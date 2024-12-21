package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.TableAppendixDTO;

import java.util.Collection;

public interface TableAppendixService {
    TableAppendixDTO findTableById(Long tableId);
    Collection<TableAppendixDTO> findAll();
    Float findTotalQuoteByCategory(String category);
    Float findTotalQuoteByCategoryAndYear(String category, int year);
}
