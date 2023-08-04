package com.asj.gestionhorarios.model.enums;

import com.asj.gestionhorarios.model.entity.Status;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class StatusTypes {
    public static final Status PENDING = new Status(1L, "PENDING");
    public static final Status IN_PROGRESS = new Status(2L, "IN_PROGRESS");
    public static final Status DONE = new Status(3L, "DONE");
    public static final Status CANCELLED = new Status(4L, "CANCELLED");
    public static final Status REVIEWING = new Status(5L, "REVIEWING");
}