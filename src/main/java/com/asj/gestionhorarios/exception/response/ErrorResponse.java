package com.asj.gestionhorarios.exception.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ErrorResponse {
    public boolean success = false;

    public final List<String> message;
    public final Integer statusCode;
    public Date timestamp = new Date();
}
