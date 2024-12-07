package com.ctlfab.condomini.service.implementation;

import com.ctlfab.condomini.DTO.CondominiumDTO;
import com.ctlfab.condomini.DTO.QuoteDTO;
import com.ctlfab.condomini.DTO.TableAppendixDTO;
import com.ctlfab.condomini.model.Condominium;
import com.ctlfab.condomini.model.Quote;
import com.ctlfab.condomini.model.TableAppendix;
import com.ctlfab.condomini.repository.CondominiumRepository;
import com.ctlfab.condomini.repository.QuoteRepository;
import com.ctlfab.condomini.repository.TableAppendixRepository;
import com.ctlfab.condomini.service.CondominiumService;
import com.ctlfab.condomini.service.QuoteService;
import com.ctlfab.condomini.service.TableAppendixService;
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
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private static final Logger logger = LoggerFactory.getLogger(QuoteServiceImpl.class);

    private final CondominiumRepository condominiumRepository;
    private final TableAppendixRepository tableAppendixRepository;
    private final TableAppendixService tableAppendixService;

    @Override
    public QuoteDTO saveQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId) {
        logger.info("Saving quote: {}", quoteDTO);

        Quote quote = mapDTOToEntity(quoteDTO, condominiumId, tableId);
        return mapEntityToDTO(quoteRepository.save(quote));
    }

    @Override
    public QuoteDTO updateQuote(QuoteDTO quoteDTO, Long condominiumId, Long tableId) {
        logger.info("Updating quote: {}", quoteDTO);

        Quote quote = mapDTOToEntity(quoteDTO, condominiumId, tableId);
        return mapEntityToDTO(quoteRepository.save(quote));
    }

    @Override
    public Boolean deleteQuote(Long quoteId) {
        logger.info("Deleting quote by ID: {}", quoteId);

        quoteRepository.deleteById(quoteId);
        return TRUE;
    }

    @Override
    public Collection<QuoteDTO> findAllQuotesByCondominium(Long condominiumId) {
        logger.info("Fetching all quotes by condominium Id: {}", condominiumId);

        Collection<QuoteDTO> quotes = new LinkedList<>();
        for(Quote quote : quoteRepository.findAllQuotesByCondominium(condominiumId)){
            quotes.add(mapEntityToDTO(quote));
        }

        return quotes;
    }

    @Override
    public QuoteDTO findQuoteByCondominiumAndTableId(Long condominiumId, Long tableId) {
        logger.info("Fetching quote by condominium and table Id: {}", condominiumId);
        Optional<Quote> quote = quoteRepository.findQuoteByCondominiumAndTableId(condominiumId ,tableId);

        return quote.map(this::mapEntityToDTO).orElse(null);

    }

    private QuoteDTO mapEntityToDTO(Quote quote) {
        TableAppendixDTO tableDTO = tableAppendixService.findTableById(quote.getTable().getId());

        return QuoteDTO.builder()
                .id(quote.getId())
                .createdAt(quote.getCreatedAt())
                .totalAmount(quote.getTotalAmount())
                .table(tableDTO)
                .build();
    }

    private Quote mapDTOToEntity(QuoteDTO quoteDTO, Long condominiumId, Long tableId) {
        Condominium condominium = condominiumRepository.findById(condominiumId).get();
        TableAppendix table = tableAppendixRepository.findById(tableId).get();

        return Quote.builder()
                .id(quoteDTO.getId())
                .createdAt(quoteDTO.getCreatedAt())
                .totalAmount(quoteDTO.getTotalAmount())
                .table(table)
                .condominium(condominium)
                .build();
    }
}
