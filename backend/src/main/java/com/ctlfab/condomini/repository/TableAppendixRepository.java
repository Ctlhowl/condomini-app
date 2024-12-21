package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.TableAppendix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface TableAppendixRepository extends JpaRepository<TableAppendix, Long> {
    @Query("SELECT SUM(t.quote.totalAmount) FROM TableAppendix t WHERE t.category = :category")
    Float findTotalQuoteByCategory(String category);

    @Query("SELECT SUM(q.totalAmount) FROM TableAppendix t JOIN t.quote q WHERE t.category = :category AND q.createdAt > :startDate AND q.createdAt < :endDate")
    Float findTotalQuoteByCategoryAndYear(String category, Timestamp startDate, Timestamp endDate);
}
