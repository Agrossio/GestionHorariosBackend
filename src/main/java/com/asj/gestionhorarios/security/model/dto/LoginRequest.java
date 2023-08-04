package com.asj.gestionhorarios.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;

}