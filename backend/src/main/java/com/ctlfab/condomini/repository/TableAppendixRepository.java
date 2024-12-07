package com.ctlfab.condomini.repository;

import com.ctlfab.condomini.model.TableAppendix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TableAppendixRepository extends JpaRepository<TableAppendix, Long> {
    @Query("SELECT t FROM TableAppendix t WHERE t.category = :category ORDER BY t.id ASC")
    List<TableAppendix> findTablebyCategories(String category);
}
