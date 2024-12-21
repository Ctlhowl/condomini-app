package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("SELECT a FROM Apartment a WHERE a.condominium.id = :condominiumId")
    List<Apartment> findApartmentsByCondominiumId(long condominiumId);

    @Query(value = "CALL new_apartment_last_year_balance(:id, :newBalance)", nativeQuery = true)
    void setNewLastYearBalance(long id, float newBalance);
}
