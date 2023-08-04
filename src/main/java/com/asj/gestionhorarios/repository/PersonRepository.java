package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByEmail(String email);
    @Query("SELECT DISTINCT p FROM Person p LEFT JOIN FETCH p.roles WHERE p.email = :email")
    Optional<Person> findByEmailWithRoles(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByRoles(Role role);

    List<Person> findByRoles(Role role);

    List<Person> findAllByDisabled(boolean isDisabled);

    @Query("SELECT p FROM Project p JOIN p.people pr WHERE pr.email = :personMail")
    List<Project> findProjectsByPersonMail(@Param("personMail") String personMail);

}

