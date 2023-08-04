package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.request.*;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService service;

    @GetMapping("/filter")
    public ResponseEntity<Response> findFilteredPersons(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String cuil,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startJobRelation,
            @RequestParam(required = false) boolean disabled,
            @RequestParam(required = false) String roleName,
            Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Users found", service.findFiltered(email, name, lastname, cuil, startJobRelation, disabled, roleName, pageable)));

    }

    @GetMapping
    public ResponseEntity<Response> findAll(@RequestParam(value = "isDisabled", required = false, defaultValue = "false") boolean isDisabled) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Users found: This endpoint is deprecated and it will be deleted in future versions, use /api/v1/person/filter with the required filters instead", service.findAllFilter(isDisabled)));
    }

    @GetMapping("/admin/by-role/{roleName}")
    public ResponseEntity<Response> findPersonsByRole(@PathVariable String roleName) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Users found by role: This endpoint is deprecated and it will be deleted in future versions, use /api/v1/person/filter with the required filters instead", service.findByRole(roleName)));
    }

    @GetMapping("{email}")
    public ResponseEntity<Response> findByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "User found by email", service.findByEmail(email)));
    }

    @GetMapping("/get-connecteds")
    public ResponseEntity<Response> getConnecteds() {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "User's connected'", service.findConnecteds()));
    }

    @PostMapping
    public ResponseEntity<Response> savePerson(@Valid @RequestBody PublicRegisterRequest person) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Person created", service.save(person)));
    }

    @DeleteMapping("{email}")
    public ResponseEntity<Response> deletePerson(@PathVariable String email) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Person deleted", service.delete(email)));
    }


    @PutMapping("/admin/add-role")
    public ResponseEntity<Response> adminUpdatePerson(@Valid @RequestBody AdminPersonUpdate update) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Role added", service.adminRolesUpdate(update)));
    }
    @PutMapping("/admin/{email}")
    public ResponseEntity<Response> adminUpdateUser(@PathVariable String email, @Valid @RequestBody AdminUserUpdateRequest update) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "User updated", service.adminPersonUpdate(email, update)));
    }

    @PutMapping("{email}")
    public ResponseEntity<Response> updatePerson(@PathVariable String email, @Valid @RequestBody PrivateUpdatePersonRequest update) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Person updated", service.personUpdate(email, update)));
    }

    @PutMapping("/password/{email}")
    public ResponseEntity<Response> updatePersonPassword(@PathVariable String email, @Valid @RequestBody UpdatePersonPasswordRequest dataToUpdate) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Person updated", service.personPasswordUpdate(email, dataToUpdate)));
    }

}
