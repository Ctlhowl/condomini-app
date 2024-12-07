package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("SELECT a FROM Apartment a WHERE a.condominium.id = :condominiumId")
    List<Apartment> findApartmentsByCondominiumId(long condominiumId);

    @Query("SELECT a FROM Outlay o JOIN o.apartment a ON a.id = o.apartment.id WHERE o.id = :outlayId")
    Apartment findApartmentsByByOutlayId(Long outlayId);
}
