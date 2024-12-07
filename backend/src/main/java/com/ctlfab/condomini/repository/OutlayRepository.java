package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Outlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OutlayRepository extends JpaRepository<Outlay, Long> {

    @Query("SELECT o FROM Outlay o WHERE o.apartment.id = :apartmentId")
    Collection<Outlay> findApartmentOutlaysByApartmentId(Long apartmentId);

    @Query("SELECT o FROM Outlay o WHERE o.condominium.id = :condominiumId and o.apartment.id IS NULL")
    Collection<Outlay> findCondominiumOutlaysByCondominiumId(Long condominiumId);

    @Query("SELECT o FROM Outlay o WHERE o.condominium.id = :condominiumId")
    Collection<Outlay> findAllOutlaysByCondominiumId(Long condominiumId);

    @Query("SELECT o FROM Outlay o WHERE o.table.id = :tableId")
    Collection<Outlay> findAllOutlaysByTableId(Long tableId);
}

