package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CondominiumRepository extends JpaRepository<Condominium, Long> {
    @Query("SELECT c FROM Condominium c ORDER BY c.id ASC")
    List<Condominium> findAllOrderById();

    @Query(value = "CALL new_condominium_last_year_balance(:id, :newBalance)", nativeQuery = true)
    void setNewLastYearBalance(long id, float newBalance);
}
