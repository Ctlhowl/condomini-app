package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Outlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Collection;

public interface OutlayRepository extends JpaRepository<Outlay, Long> {

    @Query("SELECT o FROM Outlay o WHERE o.apartment.id = :apartmentId AND o.createdAt > :startDate AND o.createdAt < :endDate ORDER BY o.id ASC")
    Collection<Outlay> findApartmentOutlaysByApartmentId(Long apartmentId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT o FROM Outlay o WHERE o.condominium.id = :condominiumId AND o.createdAt > :startDate AND o.createdAt < :endDate AND  o.apartment.id IS NULL")
    Collection<Outlay> findCondominiumOutlaysByCondominiumId(Long condominiumId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT o FROM Outlay o WHERE o.table.id = :tableId AND o.createdAt > :startDate AND o.createdAt < :endDate")
    Collection<Outlay> findAllOutlaysByTableId(Long tableId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT SUM(o.amount) FROM Outlay o WHERE o.apartment.id = :apartmentId AND o.createdAt > :startDate AND o.createdAt < :endDate")
    Float totalAmountByApartmentId(Long apartmentId, Timestamp startDate, Timestamp endDate);
}

