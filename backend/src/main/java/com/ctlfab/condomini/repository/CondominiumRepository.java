package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CondominiumRepository extends JpaRepository<Condominium, Long> {

    @Query("SELECT c FROM Condominium c JOIN c.reports r ON c.id = r.condominium.id WHERE r.id = :reportId")
    Condominium findCondominiumByReportId(Long reportId);

    @Query("SELECT c FROM Condominium c ORDER BY c.id ASC")
    List<Condominium> findAllOrderById();
}
