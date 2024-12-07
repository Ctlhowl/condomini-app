package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT q FROM Quote q  WHERE q.condominium.id = :condominiumId")
    Collection<Quote> findAllQuotesByCondominium(Long condominiumId);

    @Query("SELECT q FROM Quote q  WHERE q.table.id = :tableId")
    Quote findQuoteByTableId(Long tableId);

    @Query("SELECT q FROM Quote q  WHERE q.condominium.id = :condominiumId AND q.table.id = :tableId")
    Optional<Quote> findQuoteByCondominiumAndTableId(Long condominiumId, Long tableId);
}
