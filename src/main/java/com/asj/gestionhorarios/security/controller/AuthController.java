package com.asj.gestionhorarios.security.controller;

import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.security.model.dto.LoginRequest;
import com.asj.gestionhorarios.security.model.dto.UserResponse;
import com.asj.gestionhorarios.security.service.JWTTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthController.AUTH)
@Slf4j
public class AuthController {

    public static final String AUTH = "/api/v1/auth";
    public static final String LOGIN = "/login";

    @Autowired
    private AuthenticationManager authenticationManager;
    private final JWTTokenService jwtTokenService;

    @PostMapping(AuthController.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequestDTO) throws AuthenticationException  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserResponse userResponse = new UserResponse(jwtTokenService.generateToken(authentication),user.getUsername(), roles);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(
                true,
                "Valid credentials",
                userResponse));
    }
}
