package com.asj.gestionhorarios.data;

import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.enums.RoleTypes;
import com.asj.gestionhorarios.model.request.PublicRegisterRequest;
import com.asj.gestionhorarios.model.response.Person.PersonResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DummyData {
    /**********
     * Person *
     **********/
    public static Person getPersonOne() {
        return new Person("test@mail.com"
                , "Password1"
                , "Test"
                , "Controller"
                , "40531010"
                , "20-4052323-1"
                , 8
                , null);
    }

   public static PublicRegisterRequest getPersonRequest() {
        PublicRegisterRequest person = new PublicRegisterRequest();
        person.setName("test");
        person.setLastname("controller");
        person.setCuil("20-4564567-1");
        person.setPassword("Password1");
        person.setEmail("test@mail");
        return person;
    }

    ;

    public static PersonResponse getPersonResponse() {
        Set<Role> roleSetTest = new HashSet<>();
        roleSetTest.add(RoleTypes.BLOCKED);
        PersonResponse person = new PersonResponse();
        person.setName("test");
        person.setLastname("controller");
        person.setCuil("20-4564567-1");
        person.setEmail("test@mail");
        person.setRoles(roleSetTest);
        return person;
    }

}
