package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.BadRequestException;
import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.enums.RoleTypes;
import com.asj.gestionhorarios.model.request.*;
import com.asj.gestionhorarios.model.response.Person.PersonResponse;
import com.asj.gestionhorarios.model.response.Person.ShortPersonResponse;
import com.asj.gestionhorarios.model.response.Project.PersonHasProjectsResponse;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.security.exception.handler.WebSocketHandlerCustom;
import com.asj.gestionhorarios.service.interfaces.PersonService;
import com.asj.gestionhorarios.utils.RoleFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final WebSocketHandlerCustom ws;

    @Override
    public PersonResponse save(PublicRegisterRequest person) {
        if (repository.existsByEmail(person.getEmail()))
            throw new DataIntegrityViolationException("This user already exist.");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person personToSave = PublicRegisterRequest.toEnty(person);
        personToSave.setRoles(new HashSet<>(List.of(RoleTypes.BLOCKED)));
        return PersonResponse.toResponse(repository.save(personToSave));
    }

    @Transactional
    @Override
    public PersonResponse findByEmail(String email) {
        Person personFound = repository.findByEmailWithRoles(email).orElseThrow(() -> new NotFoundException("User not found."));
        PersonResponse personResponse = PersonResponse.toResponse(personFound);
        List<PersonHasProjectsResponse> projectOfPerson = PersonHasProjectsResponse.toResponseList(repository.findProjectsByPersonMail(email));
        personResponse.setProjects(projectOfPerson);
        return personResponse;
    }

    @Override
    public String delete(String email) {
        try {
            Person personFound = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found."));
            repository.delete(personFound);
            return "Person deleted";
        } catch (PersistenceException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public PersonResponse adminRolesUpdate(AdminPersonUpdate update) {
        Person person = repository.findByEmail(update.getEmail()).orElseThrow(() -> new NotFoundException("User not found."));
        person.getRoles().remove(RoleTypes.BLOCKED);
        update.getRoleNames()
                .forEach(rolName -> {
                    Role newRole = RoleFactory.newRole(rolName);
                    if (person.getRoles().stream()
                            .noneMatch(rol -> rol.getRole_name().equals(newRole.getRole_name()))) {
                        person.getRoles().add(newRole);
                    }

                });
        if (StringUtils.hasLength(String.valueOf(update.getStart_job_relation())))
            person.setStart_job_relation(update.getStart_job_relation());
        return PersonResponse.toResponse(repository.save(person));
    }

    @Override
    public List<ShortPersonResponse> findByRole(String roleName) {
        List<Person> blocked = repository.findByRoles(RoleFactory.newRole(roleName));
        return blocked.stream()
                .map(ShortPersonResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PersonResponse personUpdate(String email, PrivateUpdatePersonRequest update) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        if (!authenticatedEmail.equals(email)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this user.");
        }
        Person person = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found."));
        person.setName(update.getName());
        person.setLastname(update.getLastname());
        person.setCuil(update.getCuil());
        person.setTel(update.getTel());
        person.setHours_journal(update.getHours_journal());
        person.setImage(update.getImage());
        return PersonResponse.toResponse(repository.save(person));
    }

    @Override
    public PersonResponse adminPersonUpdate(String email, AdminUserUpdateRequest userUpdateRequest) {
        Person person = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found."));
        person.getRoles().removeIf(element -> true);
        userUpdateRequest.getRoleNames()
                .forEach(rolName -> {
                    Role newRole = RoleFactory.newRole(rolName);
                    if (person.getRoles().stream()
                            .noneMatch(rol -> rol.getRole_name().equals(newRole.getRole_name()))) {
                        person.getRoles().add(newRole);
                    }
                });

        person.setName(userUpdateRequest.getName());
        person.setLastname(userUpdateRequest.getLastname());
        person.setCuil(userUpdateRequest.getCuil());
        person.setTel(userUpdateRequest.getTel());
        person.setHours_journal(userUpdateRequest.getHours_journal());
        person.setStart_job_relation(userUpdateRequest.getStart_job_relation());
        return PersonResponse.toResponse(repository.save(person));
    }

    @Override
    public PersonResponse personPasswordUpdate(String email, UpdatePersonPasswordRequest dataToUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        if (!authenticatedEmail.equals(email)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this user.");
        }
        Person person = repository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found."));
        if (passwordEncoder.matches(dataToUpdate.getOldPassword(), person.getPassword())) {
            String hashedPassword = passwordEncoder.encode(dataToUpdate.getNewPassword());
            person.setPassword(hashedPassword);
            return PersonResponse.toResponse(repository.save(person));
        } else {
            throw new BadRequestException("Invalid credentials");
        }
    }

    @Override
    public List<PersonResponse> findAllFilter(boolean isDisabled) {
        List<PersonResponse> personFound = repository.findAllByDisabled(isDisabled).stream()
                .map(PersonResponse::toResponse)
                .map(person -> {
                    person.setProjects(PersonHasProjectsResponse.toResponseList(repository.findProjectsByPersonMail(person.getEmail())));
                    return person;
                })
                .collect(Collectors.toList());
        return personFound;
    }

    @Override
    public Page<PersonResponse> findFiltered(String email, String name, String lastname, String cuil, LocalDate start_job_relation, boolean disabled, String roleName, Pageable pageable) {

        Specification<Person> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            // TODO: use a Switch Case:
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (lastname != null && !lastname.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("lastname"), "%" + lastname + "%"));
            }

            if (cuil != null && !cuil.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("cuil"), "%" + cuil + "%"));
            }

            if (start_job_relation != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("start_job_relation"), start_job_relation));
            }

            predicates.add(criteriaBuilder.equal(root.get("disabled"), disabled));

            if (roleName != null && !roleName.isEmpty()) {

                Join<Person, Role> roleJoin = root.join("roles");
                predicates.add(criteriaBuilder.equal(roleJoin.get("role_name"), roleName));

            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<Person> personsPage = repository.findAll(spec, pageable);

        List<PersonResponse> personsResponse = personsPage.stream()
                .map(PersonResponse::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(personsResponse, pageable, personsPage.getTotalElements());
    }

    @Override
    public List<String> findConnecteds() {
        return ws.obtenerNumeroSesionesConectadas();
    }
}