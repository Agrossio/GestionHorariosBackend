package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("from Role r where r.role_name = ?1")
    Optional<Role> findByRoleName(String role_name);

    @Query(value = "SELECT r.role_name FROM role r INNER JOIN person_roles pr ON r.role_id = pr.roles_role_id INNER JOIN person p ON p.email = pr.person_email WHERE p.email = ?", nativeQuery = true)
    List<String> findRoleNamesByEmail(String email);
}
