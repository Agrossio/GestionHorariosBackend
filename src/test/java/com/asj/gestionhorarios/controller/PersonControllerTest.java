package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.data.DummyData;
import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.request.AdminPersonUpdate;
import com.asj.gestionhorarios.model.request.PrivateUpdatePersonRequest;
import com.asj.gestionhorarios.model.request.UpdatePersonPasswordRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.model.response.Person.PersonResponse;
import com.asj.gestionhorarios.model.response.Person.ShortPersonResponse;
import com.asj.gestionhorarios.service.interfaces.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



class PersonControllerTest {
    private PersonController personController;
    @Mock
    private PersonService personService;

    private Person dummyPerson;
    private PersonResponse dummyPersonResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        personController = new PersonController(personService);
        dummyPerson = DummyData.getPersonOne();
        dummyPersonResponse = PersonResponse.toResponse(dummyPerson);
    }

    @Test
    void findByEmail() {
        // GIVEN
        when(personService.findByEmail(dummyPerson.getEmail())).thenReturn(dummyPersonResponse);
        when(personService.findByEmail("nonexistent@mail.com")).thenThrow(new NotFoundException("User not found"));
        // WHEN
        ResponseEntity<Response> response = personController.findByEmail(dummyPerson.getEmail());

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, Objects.requireNonNull(response.getBody()).isSuccess());
        assertEquals("Person found by email", response.getBody().getMessage());
        assertEquals(dummyPersonResponse, response.getBody().getData());
        assertThatThrownBy(() -> personController.findByEmail("nonexistent@mail.com")).isInstanceOf(NotFoundException.class);

    }

    @Test
    void savePerson() {
        //GIVEN
        when(personService.save(DummyData.getPersonRequest())).thenReturn(dummyPersonResponse);

        //WHEN
        ResponseEntity<Response> response = personController.savePerson(DummyData.getPersonRequest());

        //THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Person created", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(dummyPersonResponse, response.getBody().getData());
    }

    @Test
    void deletePerson() throws AccessDeniedException {
        // GIVEN
        when(personService.delete(dummyPerson.getEmail())).thenReturn("Person deleted");

        // WHEN
        ResponseEntity<Response> response = personController.deletePerson(dummyPerson.getEmail());

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Person deleted", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals("Person deleted", response.getBody().getData());
    }

    @Test
    void adminUpdatePerson() {
        // GIVEN
        AdminPersonUpdate update = new AdminPersonUpdate();
        when(personService.adminRolesUpdate(update)).thenReturn(dummyPersonResponse);

        // WHEN
        ResponseEntity<Response> response = personController.adminUpdatePerson(update);

        // THEN
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Role added", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(dummyPersonResponse, response.getBody().getData());
    }

    @Test
    void findPersonByRole() {
        // GIVEN
        List<ShortPersonResponse> personsList = new ArrayList<>();
        personsList.add(ShortPersonResponse.toResponse(dummyPerson));
        when(personService.findByRole("test")).thenReturn(personsList);

        // WHEN
        ResponseEntity<Response> response = personController.findPersonsByRole("test");

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test persons", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(personsList, response.getBody().getData());
    }
   /* @Test
    void updatePerson() {
        // GIVEN
        PrivateUpdatePersonRequest update = new PrivateUpdatePersonRequest();
        when(personService.personUpdate(dummyPerson.getEmail(), update)).thenReturn(dummyPersonResponse);

        // WHEN
        ResponseEntity<Response> response = personController.updatePerson(dummyPerson.getEmail(),update);

        // THEN
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Person updated", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(dummyPersonResponse, response.getBody().getData());
    }*/
   /* @Test
    void updatePersonPassword() {
        // GIVEN
        UpdatePersonPasswordRequest update = new UpdatePersonPasswordRequest();
        when(personService.personPasswordUpdate(dummyPerson.getEmail(), update)).thenReturn(dummyPersonResponse);

        // WHEN
        ResponseEntity<Response> response = personController.updatePersonPassword(dummyPerson.getEmail(),update);

        // THEN
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Person updated", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(dummyPersonResponse, response.getBody().getData());
    }*/
    @Test
    void findAll() {
        // GIVEN
        List<PersonResponse> personsList = new ArrayList<>();
        personsList.add(PersonResponse.toResponse(dummyPerson));
        when(personService.findAllFilter(false)).thenReturn(personsList);

        // WHEN
        ResponseEntity<Response> response = personController.findAll(false);

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Persons found", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(personsList, response.getBody().getData());
    }
}