package com.asj.gestionhorarios.service;

import com.asj.gestionhorarios.data.DummyData;
import com.asj.gestionhorarios.exception.customExceptions.BadRequestException;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.enums.RoleTypes;
import com.asj.gestionhorarios.model.request.AdminPersonUpdate;
import com.asj.gestionhorarios.model.request.PrivateUpdatePersonRequest;
import com.asj.gestionhorarios.model.request.PublicRegisterRequest;
import com.asj.gestionhorarios.model.request.UpdatePersonPasswordRequest;
import com.asj.gestionhorarios.model.response.Person.PersonResponse;
import com.asj.gestionhorarios.model.response.Person.ShortPersonResponse;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.security.exception.handler.WebSocketHandlerCustom;
import com.asj.gestionhorarios.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {PersonServiceImpl.class})
class PersonServiceImplTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private PersonServiceImpl personService;
    private Person person;
    private PublicRegisterRequest personToRegister;
    private WebSocketHandlerCustom ws;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl(personRepository, passwordEncoder,ws);
        person = DummyData.getPersonOne();
        personToRegister = DummyData.getPersonRequest();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void save() {
        //GIVEN
        PublicRegisterRequest existingUser = PublicRegisterRequest.builder().email("existingMail@mail.com").build();
        PublicRegisterRequest persistenceErrorTestUser = PublicRegisterRequest.builder().password("fakeUserPassword").build();
        Person savedPerson = Person.builder().roles(Collections.singleton(RoleTypes.BLOCKED)).build();
        when(personRepository.existsByEmail(anyString())).thenReturn(false);
        when(personRepository.existsByEmail(existingUser.getEmail())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("EncryptedPassword");
        when(passwordEncoder.encode(persistenceErrorTestUser.getPassword())).thenThrow(new PersistenceException());
        when(personRepository.save(any())).thenReturn(savedPerson);

        //WHEN
        PersonResponse personSaved = personService.save(personToRegister);

        //THEN
        assertThat(personSaved).isInstanceOf(PersonResponse.class);
        assertThatThrownBy(() -> personService.save(existingUser)).isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> personService.save(persistenceErrorTestUser)).isInstanceOf(PersistenceException.class);
    }


    @Test
    void findByEmail() {
        //GIVEN
        String invalidMail = "invalidMail@mail.com";
        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(personRepository.findByEmail(invalidMail)).thenThrow(new PersistenceException());
        //WHEN
        PersonResponse personFound = personService.findByEmail("test");

        //THEN
        assertThat(personFound).isInstanceOf(PersonResponse.class);
        assertThatThrownBy(() -> personService.findByEmail(invalidMail)).isInstanceOf(PersistenceException.class);
    }

    @Test
    void delete() {
        //GIVEN
        String invalidMail = "invalidMail@mail.com";
        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(personRepository.findByEmail(invalidMail)).thenThrow(new PersistenceException());

        //WHEN
        String successMessage = personService.delete(person.getEmail());

        //THEN
        assertThat(successMessage).isEqualTo("Person deleted");
        assertThatThrownBy(() -> personService.delete(invalidMail)).isInstanceOf(PersistenceException.class);
    }

    @Test
    void adminRolesUpdate() {
        //GIVEN
        String invalidMail = "invalidMail@mail.com";
        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(personRepository.findByEmail(invalidMail)).thenThrow(new PersistenceException());
        person.setRoles(new HashSet<>(List.of(RoleTypes.BLOCKED)));
        AdminPersonUpdate rolesToAddAndUser = new AdminPersonUpdate(
                person.getEmail()
                , List.of("DEVELOPER")
                , LocalDate.now());
        AdminPersonUpdate persistenceExceptionUser = new AdminPersonUpdate(
                invalidMail
                , List.of("DEVELOPER")
                , LocalDate.now());
        when(personRepository.save(person)).thenReturn(person);
        //WHEN
        PersonResponse updatedPerson = personService.adminRolesUpdate(rolesToAddAndUser);

        //THEN
        assertThat(updatedPerson).isInstanceOf(PersonResponse.class);
        assertThatThrownBy(() -> personService.adminRolesUpdate(persistenceExceptionUser)).isInstanceOf(PersistenceException.class);
    }

    @Test
    void findByRole() {
        //GIVEN
        when(personRepository.findByRoles(RoleTypes.DEVELOPER)).thenReturn(Arrays.asList(person, person));
        when(personRepository.findByRoles(RoleTypes.MANAGEMENT)).thenThrow(new PersistenceException("Test Persistence Exception"));
        //In this method, management role should be valid, but to test the persistence exception I must use a role type
        //and I think it's better to use an existing one than to create a new one for testing purposes

        //WHEN
        List<ShortPersonResponse> personsFoundByRole = personService.findByRole("DEVELOPER");

        //THEN
        assertThat(personsFoundByRole).isInstanceOf(List.class);
        assertThatThrownBy(() -> personService.findByRole("MANAGEMENT")).isInstanceOf(PersistenceException.class);
    }

    /*@Test
    void personUpdate() {
        //GIVEN
        PrivateUpdatePersonRequest updatedPerson = new PrivateUpdatePersonRequest(
                "Changed"
                , "Name"
                , "changed@mail.com"
                , "changedCuil"
                , "changedTel"
                , 2
                , null);
        Person personUpdated = new Person(
                updatedPerson.getEmail()
                , person.getPassword()
                , updatedPerson.getName()
                , updatedPerson.getLastname()
                , updatedPerson.getTel()
                , updatedPerson.getCuil()
                , updatedPerson.getHours_journal()
                , updatedPerson.getImage());
        when(personRepository.save(person)).thenReturn(personUpdated);
        when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("PersistenceExceptionTest")).thenThrow(new PersistenceException());
        when(personRepository.save(person)).thenReturn(person);

        //WHEN
        PersonResponse personPostUpdate = personService.personUpdate(person.getEmail(), updatedPerson);

        //THEN
        assertThat(personPostUpdate).isInstanceOf(PersonResponse.class);
        assertThat(personPostUpdate.getName()).isEqualTo(personUpdated.getName());
        assertThatThrownBy(() -> personService.personUpdate("PersistenceExceptionTest", updatedPerson)).isInstanceOf(PersistenceException.class);
    }*/

   /* @Test
    void personPasswordUpdate() {
        //GIVEN
        UpdatePersonPasswordRequest oldAndNewPassword = new UpdatePersonPasswordRequest(person.getPassword(), "newPassword1");
        UpdatePersonPasswordRequest wrongOldPassword = new UpdatePersonPasswordRequest("wrongPassword", "newPassword1");
        when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(oldAndNewPassword.getOldPassword(), person.getPassword())).thenReturn(true);
        when(passwordEncoder.matches("wrongPassword", oldAndNewPassword.getNewPassword())).thenReturn(false);
        when(personRepository.findByEmail("PersistenceExceptionTest")).thenThrow(new PersistenceException());
        when(personRepository.save(person)).thenReturn(person);

        //WHEN
        PersonResponse personUpdated = personService.personPasswordUpdate(person.getEmail(), oldAndNewPassword);

        //THEN
        assertThat(personUpdated).isInstanceOf(PersonResponse.class);
        assertThatThrownBy(() -> personService.personPasswordUpdate(person.getEmail(), wrongOldPassword)).isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> personService.personPasswordUpdate("PersistenceExceptionTest", oldAndNewPassword)).isInstanceOf(PersistenceException.class);
    }*/

    @Test
    void findAllFilter() {
        //GIVEN
        Person disabledPerson = new Person(
                "disabled@mail.com"
                , "Disabled1"
                , "Disabled"
                , "Person"
                , "DisabledTel"
                , "DisabledCuil"
                , 8, LocalDate.now()
                , null
                , true
                , new HashSet<>(List.of(RoleTypes.DEVELOPER)));
        when(personRepository.findAllByDisabled(false)).thenReturn(Collections.singletonList(person));
        when(personRepository.findAllByDisabled(true)).thenReturn(Collections.singletonList(disabledPerson));

        //WHEN
        List<PersonResponse> allpersonsList = personService.findAllFilter(false);
        List<PersonResponse> filteredPersonsList = personService.findAllFilter(true);

        //THEN
        List<PersonResponse> allPersonsResponseList = new ArrayList<>();
        allPersonsResponseList.add(PersonResponse.toResponse(person));
        List<PersonResponse> disabledPersonsResponseList = new ArrayList<>();
        disabledPersonsResponseList.add(PersonResponse.toResponse(disabledPerson));

        assertThat(allpersonsList).isEqualTo(allPersonsResponseList);
        assertThat(filteredPersonsList).isEqualTo(disabledPersonsResponseList);
    }

    @Test
    void findAllFilterPersistenceException(){
        when(personRepository.findAllByDisabled(anyBoolean())).thenThrow(new PersistenceException());
        assertThatThrownBy(()->personService.findAllFilter(anyBoolean())).isInstanceOf(PersistenceException.class);
    }
}
