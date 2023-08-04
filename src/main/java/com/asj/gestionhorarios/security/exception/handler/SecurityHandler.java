package com.asj.gestionhorarios.security.exception.handler;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@Component
public class SecurityHandler {
    public void handle(Exception e, HttpServletResponse response) throws IOException {
        System.out.println("SecurityHandler.handle");
        if (response.getStatus() != 200) return;
        int statusCode;
        String message = "Authentication failed";
        if (e instanceof ExpiredJwtException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            log.warn("Token expired");
        } else if (e instanceof SignatureException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            log.warn("Invalid token signature");
        } else if (e instanceof IllegalArgumentException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            log.warn("Invalid token");
        } else if (e instanceof MalformedJwtException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            log.warn("Malformed token");
        } else if (e instanceof HttpMessageNotReadableException) {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            log.warn("Invalid request message");
        } else if (e instanceof AccessDeniedException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            message = "Access denied";
        } else if (e instanceof AuthenticationCredentialsNotFoundException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            log.warn("Authentication credentials not found");
        } else if (e instanceof BadCredentialsException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            log.warn("Bad credentials");
        } else if (e instanceof UsernameNotFoundException) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            log.warn("Username not found");
        } else if (e instanceof InsufficientAuthenticationException) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            log.warn("Insufficient authentication");
            message = "Token needed";
        } else if (e instanceof NotFoundException) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            log.warn(e.getMessage());
            message = e.getMessage();
        } else {
            System.out.println("SecurityHandler.handle");
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            message = "Server error, cannot catch exception" + e.getMessage();
            log.warn("Server error, cannot catch exception");
            //// TODO: 6/6/2023 Este codigo sirve para visualizar las excepciones que no se pudieron manejar, principalmente
            //TODO aquellas que estan relacionadas con errores de codigo
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            log.error("Error calling:"
                    +"\nError description: "+e.getMessage()
                    +"\nTrack trace:"+stackTrace);
        }

        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        new ErrorResponse(List.of(message), statusCode)));
    }
}