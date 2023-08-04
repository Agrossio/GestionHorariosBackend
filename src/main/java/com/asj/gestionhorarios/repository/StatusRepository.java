package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("from Status s where s.status_name = ?1")
    Optional<Status> findByStatus_name(String status_name);
}
