package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query("SELECT q FROM Quote q  WHERE q.condominium.id = :condominiumId AND q.createdAt > :startDate AND q.createdAt < :endDate ORDER BY q.id ASC")
    Collection<Quote> findAllQuotesByCondominium(Long condominiumId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT q FROM Quote q  WHERE q.condominium.id = :condominiumId AND q.table.id = :tableId  AND q.createdAt > :startDate AND q.createdAt < :endDate")
    Optional<Quote> findQuoteByCondominiumAndTableId(Long condominiumId, Long tableId, Timestamp startDate, Timestamp endDate);
}
