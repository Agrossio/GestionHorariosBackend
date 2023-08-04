package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.config.TestConfig;
import com.asj.gestionhorarios.data.DummyData;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(TestConfig.class)
class PersonRepositoryTest {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Autowired
    PersonRepositoryTest(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    void setUp() {
        personRepository.save(DummyData.getPersonOne());
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        Optional<Person> foundPerson = personRepository.findByEmail(DummyData.getPersonOne().getEmail());
        assertThat(foundPerson).isPresent();
        foundPerson.ifPresent(person -> assertThat(person.getEmail()).isEqualTo(DummyData.getPersonOne().getEmail()));
    }

    @Test
    void existByEmail() {
        boolean userExist = personRepository.existsByEmail(DummyData.getPersonOne().getEmail());
        boolean userDoesNotExist = personRepository.existsByEmail("error");

        assertThat(userExist).isTrue();
        assertThat(userDoesNotExist).isFalse();
    }

    @Test
    void existsByRoles() {
        Person personTest = DummyData.getPersonOne();
        Role roleTest = new Role("Test");
        Set<Role> roleSetTest = new HashSet<>();
        roleSetTest.add(roleTest);
        personTest.setRoles(roleSetTest);
        roleRepository.save(roleTest);
        personRepository.save(personTest);
        boolean test = personRepository.existsByRoles(roleTest);
        assertThat(test).isTrue();
    }

    @Test
    void findByRoles() {
        Person personTest = DummyData.getPersonOne();
        Role roleTest = new Role("Test");
        Set<Role> roleSetTest = new HashSet<>();
        roleSetTest.add(roleTest);
        personTest.setRoles(roleSetTest);
        roleRepository.save(roleTest);
        personRepository.save(personTest);
        List<Person> personsFound = personRepository.findByRoles(roleTest);
        assertThat(personsFound.size()).isEqualTo(1);
    }

    @Test
    void findAllByDisabled() {
        Role roleTest = new Role("Test");
        Set<Role> roleSetTest = new HashSet<>();
        roleSetTest.add(roleTest);
        roleRepository.save(roleTest);
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
                , roleSetTest);
        personRepository.save(disabledPerson);
        List<Person> disabledPersons = personRepository.findAllByDisabled(true);
        List<Person> allPersons = personRepository.findAllByDisabled(false);
        assertThat(disabledPersons.size()).isEqualTo(1);
        assertThat(allPersons.size()).isEqualTo(1);
    }
}
