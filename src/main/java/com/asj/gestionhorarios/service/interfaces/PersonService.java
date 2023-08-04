package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.request.*;
import com.asj.gestionhorarios.model.response.Person.PersonResponse;
import com.asj.gestionhorarios.model.response.Person.ShortPersonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface PersonService {
    PersonResponse save(PublicRegisterRequest person);

    PersonResponse findByEmail(String email);

    String delete(String email) throws AccessDeniedException;

    PersonResponse adminRolesUpdate(AdminPersonUpdate update);

    PersonResponse adminPersonUpdate(String email, AdminUserUpdateRequest userUpdateRequest);

    List<ShortPersonResponse> findByRole(String role);

    PersonResponse personUpdate(String email, PrivateUpdatePersonRequest update) throws AccessDeniedException;

    PersonResponse personPasswordUpdate(String email, UpdatePersonPasswordRequest dataToUpdate) throws AccessDeniedException;

    List<PersonResponse> findAllFilter(boolean isDeleted);
    Page<PersonResponse> findFiltered(String email, String name, String lastname, String cuil, LocalDate start_job_relation, boolean disabled, String roleName, Pageable pageable);

    List<String> findConnecteds();
}
