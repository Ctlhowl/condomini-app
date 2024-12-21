package com.ctlfab.condomini.service;

import com.ctlfab.condomini.dto.QuoteDTO;

import java.util.Collection;

public interface QuoteService {
    QuoteDTO saveQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId);
    QuoteDTO updateQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId);
    Boolean deleteQuote(Long quoteId);

    Collection<QuoteDTO> findAllQuotesByCondominium(Long condominiumId);
    Collection<QuoteDTO> findAllQuotesByCondominiumAndYear(Long condominiumId, int year);

    QuoteDTO findQuoteByCondominiumAndTableId(Long condominiumId, Long tableId);
    QuoteDTO findQuoteByCondominiumAndTableIdAndYear(Long condominiumId, Long tableId, int year);
}
