package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Month;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthRepository extends JpaRepository<Month, Integer> {
    Optional<Month> findByYearAndMonth(Integer year, Integer month);
}
