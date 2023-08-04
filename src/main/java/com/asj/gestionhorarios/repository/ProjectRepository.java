package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query("SELECT p FROM Project p WHERE p.client.client_id = :clientId")
    List<Project> findProjectsByClientId(@Param("clientId") Long clientId);
    List<Project> findAllByDisabled(boolean isDisabled);
    boolean existsByName(String name);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Project p WHERE p.project_id = :project_id")
    boolean existsByProject_id(@Param("project_id") Long project_id);
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.people WHERE p.project_id = :projectId")
    Optional<Project> findProjectWithPeople(@Param("projectId") Long projectId);
}
