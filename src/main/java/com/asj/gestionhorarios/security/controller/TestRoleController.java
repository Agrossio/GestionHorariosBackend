package com.asj.gestionhorarios.security.controller;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestRoleController {
    @Autowired
    private PersonRepository personRepository;
    @GetMapping("/person/{email}")
    public ResponseEntity<?> testPerson(@PathVariable String email) {
        Person person = personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Person no found"));
        System.out.println("TestRoleController.testPerson"+person);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @GetMapping("/developer")
    public ResponseEntity<?> testDeveloper() {
        return ResponseEntity.status(HttpStatus.OK).body("autorizado");
    }
    @GetMapping("/admin")
    public ResponseEntity<?> testAdmin() {
        return ResponseEntity.status(HttpStatus.OK).body("autorizado");
    }
    @GetMapping("/management")
    public ResponseEntity<?> testManagement() {
        return ResponseEntity.status(HttpStatus.OK).body("autorizado");
    }
    @GetMapping("/blocked")
    public ResponseEntity<?> testBlocked() {
        return ResponseEntity.status(HttpStatus.OK).body("autorizado");
    }
    @GetMapping("/pending")
    public ResponseEntity<?> testPending() {
        return ResponseEntity.status(HttpStatus.OK).body("autorizado");
    }
}
