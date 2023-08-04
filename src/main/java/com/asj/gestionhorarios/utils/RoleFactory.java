package com.asj.gestionhorarios.utils;

import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.enums.RoleTypes;

public class RoleFactory {
    public static Role newRole(String roleName) {
        switch (roleName) {
            case "ADMIN":
                return RoleTypes.ADMIN;
            case "MANAGEMENT":
                return RoleTypes.MANAGEMENT;
            case "DEVELOPER":
                return RoleTypes.DEVELOPER;
            case "BLOCKED":
                return RoleTypes.BLOCKED;
            case "PENDING":
                return RoleTypes.PENDING;
            default:
                throw new IllegalArgumentException("Invalid role name: " + roleName);
        }
    }
}
