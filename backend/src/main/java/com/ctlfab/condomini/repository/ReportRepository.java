package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.condominium.id = :condominiumId")
    List<Report> findAllReportByCondominiumId(Long condominiumId);

    @Query("SELECT SUM(t.quote.totalAmount) FROM TableAppendix t WHERE t.category = :category")
    Float findTotalQuoteByCategory(String category);
}
