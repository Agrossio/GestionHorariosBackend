package com.asj.gestionhorarios.model.enums;

import com.asj.gestionhorarios.model.entity.Priority;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class PriorityTypes {
    public static final Priority HIGH = new Priority(1L, "HIGH");
    public static final Priority MEDIUM = new Priority(2L, "MEDIUM");
    public static final Priority LOW = new Priority(3L, "LOW");


}