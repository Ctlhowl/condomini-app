package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("SELECT a FROM Apartment a WHERE a.condominium.id = :condominiumId ORDER BY a.id ASC")
    List<Apartment> findApartmentsByCondominiumId(long condominiumId);
}
