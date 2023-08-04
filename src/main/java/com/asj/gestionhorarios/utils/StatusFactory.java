package com.asj.gestionhorarios.utils;

import com.asj.gestionhorarios.model.entity.Status;
import com.asj.gestionhorarios.model.enums.StatusTypes;

public class StatusFactory {
    public static Status newStatus(String status_name) {
        switch (status_name) {
            case "PENDING":
                return StatusTypes.PENDING;
            case "IN_PROGRESS":
                return StatusTypes.IN_PROGRESS;
            case "DONE":
                return StatusTypes.DONE;
            case "CANCELLED":
                return StatusTypes.CANCELLED;
            case "REVIEWING":
                return StatusTypes.REVIEWING;
            default:
                throw new IllegalArgumentException("Invalid role name: " + status_name);
        }
    }
}
