package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.response.Sprint.SprintResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
//    @Query("SELECT s FROM Sprint s WHERE s.project_id = :project_id AND s.disabled = :isDisabled")
//    Page<Sprint> findAllByProjectIdAndDisabledPaginated(@Param("project_id") Long project_id, @Param("isDisabled") boolean isDisabled, Pageable pageable);

    @Query("SELECT s FROM Sprint s WHERE s.project_id = :project_id")
    Page<Sprint> findAllByProjectIdPaginated(@Param("project_id") Long project_id, Pageable pageable);

}
