package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface CondominiumRepository extends JpaRepository<Condominium, Long> {

    @Query("SELECT c FROM Outlay o JOIN o.condominium c ON c.id = o.condominium.id WHERE o.id = :outlayId")
    Condominium findCondominiumByOutlayId(Long outlayId);

    @Query("SELECT c FROM Condominium c JOIN c.reports r ON c.id = r.condominium.id WHERE r.id = :reportId")
    Condominium findCondominiumByReportId(Long reportId);
}
