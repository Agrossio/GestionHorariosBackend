package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService service;
    @GetMapping("/{email}")
    public ResponseEntity<Response> getRoleByEmail(@PathVariable String email) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response(Boolean.TRUE, "Role by email", service.findRoleNamesByEmail(email)));
    }
}