package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    @Query("SELECT c FROM Client c WHERE c.email = :email OR c.business_name = :business_name")
    Client clientExistByBusinessName(@Param("email") String email, @Param("business_name") String business_name);

    List<Client> findAllByDisabled(boolean isDisabled);

}

