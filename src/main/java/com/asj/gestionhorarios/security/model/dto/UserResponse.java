package com.asj.gestionhorarios.security.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class UserResponse {
    private String token;
    private String email;
    private List<String> roles;

    public UserResponse(String token, String email, List<String> roles) {
        this.token = token;
        this.email = email;
        this.roles = roles;
    }
}
