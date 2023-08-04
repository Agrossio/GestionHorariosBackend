package com.asj.gestionhorarios.utils;

import com.asj.gestionhorarios.model.entity.Priority;
import com.asj.gestionhorarios.model.enums.PriorityTypes;

public class PriorityFactory {
    public static Priority newPriority(String priority_name) {
        switch (priority_name) {
            case "HIGH":
                return PriorityTypes.HIGH;
            case "MEDIUM":
                return PriorityTypes.MEDIUM;
            case "LOW":
                return PriorityTypes.LOW;
            default:
                throw new IllegalArgumentException("Invalid role name: " + priority_name);
        }
    }
}
