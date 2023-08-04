package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findAllByProjectAndDisabled(Project project, boolean disabled);
    List<Task> findAllByProjectAndDisabledAndPersonIsNull(Project project, boolean disabled);
    Page<Task> findAllByProjectAndDisabled(Project project, boolean disabled, Pageable pageable);
    Page<Task> findAllByProjectAndDisabledAndPersonIsNull(Project project, boolean disabled, Pageable pageable);
    List<Task> findAllBySprint(Sprint sprint);
}
