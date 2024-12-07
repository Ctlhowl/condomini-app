package com.ctlfab.condomini.service;

import com.ctlfab.condomini.DTO.QuoteDTO;

import java.util.Collection;

public interface QuoteService {
    QuoteDTO saveQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId);
    QuoteDTO updateQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId);
    Boolean deleteQuote(Long quoteId);
    Collection<QuoteDTO> findAllQuotesByCondominium(Long condominiumId);
    QuoteDTO findQuoteByCondominiumAndTableId(Long condominiumId, Long tableId);
}
