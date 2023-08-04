package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.data.DummyData;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.service.interfaces.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PersonServiceImplTest {

    @Autowired
    private PersonService service;
    @MockBean
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
    }

    @Test
    void findByEmail() {
        //Given
        Person person = DummyData.getPersonOne();

        //When
        service.findByEmail(person.getEmail());

        //Then
        assertThat(person.getEmail()).isEqualTo("test@mail");
        verify(repository).findById(person.getEmail());
    }

    @Test
    void delete() {
    }

    @Test
    void adminRolesUpdate() {
    }

    @Test
    void findByRole() {
    }

    @Test
    void personUpdate() {
    }

    @Test
    void findAllFilter() {
    }

    @Test
    void personExists() {
    }

    @Test
    void findById() {
    }

    @Test
    void roleIsPresent() {
    }
}