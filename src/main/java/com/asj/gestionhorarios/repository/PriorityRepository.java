package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Long> {
    @Query("from Priority p where p.priority_name = ?1")
    Optional<Priority> findByPriority_name(String priority_name);
}
