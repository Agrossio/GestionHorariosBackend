package com.asj.gestionhorarios.model.response.Generic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    public boolean success;
    public String message;
    public Object data;
}