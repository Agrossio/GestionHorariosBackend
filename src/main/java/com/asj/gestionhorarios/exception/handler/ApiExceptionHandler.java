package com.asj.gestionhorarios.exception.handler;

import com.asj.gestionhorarios.exception.customExceptions.BadRequestException;
import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.validation.FieldError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    /*************************************
     *         4** - Client Error         *
     *************************************/
    // FRONT VALIDATIONS //
    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, NumberFormatException.class, ParseException.class, DateTimeParseException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception e, HandlerMethod handlerMethod) {
        log.warn(e.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");

        List<String> errorsList = new ArrayList<>();
        if (e instanceof MethodArgumentNotValidException) {
            errorsList = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
        } else {
            errorsList.add("Parameter format invalid in URL");
        }

        return new ErrorResponse(errorsList, HttpStatus.BAD_REQUEST.value());
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({BadCredentialsException.class})
    @ResponseBody
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e, HandlerMethod handlerMethod){
        log.warn(e.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");
        return new ErrorResponse(Collections.singletonList(e.getMessage()), HttpStatus.FORBIDDEN.value());
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException(AccessDeniedException e, HandlerMethod handlerMethod){
        log.warn(e.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");
        return new ErrorResponse(Collections.singletonList(e.getMessage()), HttpStatus.FORBIDDEN.value());
    }
    // 404 NOT FOUND //
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException e, HandlerMethod handlerMethod){
        log.warn(e.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");
        return new ErrorResponse(Collections.singletonList(e.getMessage()), HttpStatus.NOT_FOUND.value());
    }
    // 409 CONFLICT //
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorResponse conflict(DataIntegrityViolationException e, HandlerMethod handlerMethod) {
        log.warn(e.getMessage() + " Error calling: " + handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName() + "().");
        return new ErrorResponse(Arrays.asList(e.getMessage()), HttpStatus.CONFLICT.value());
    }
    /*************************************
     *        5** - Server Error         *
     *************************************/
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ErrorResponse serverError(Exception e) {
        //// TODO: 6/6/2023 Este codigo sirve para visualizar las excepciones que no se pudieron manejar, principalmente
        //TODO aquellas que estan relacionadas con errores de codigo
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        log.error("Error calling:"
                +"\nError description: "+e.getMessage()
                +"\nTrack trace:"+stackTrace);
        return new ErrorResponse(Arrays.asList(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
