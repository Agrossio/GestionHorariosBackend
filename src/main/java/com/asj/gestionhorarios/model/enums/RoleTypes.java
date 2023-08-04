package com.asj.gestionhorarios.model.enums;

import com.asj.gestionhorarios.model.entity.Role;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class RoleTypes {
    public static final Role ADMIN = new Role(1L, "ADMIN");
    public static final Role MANAGEMENT = new Role(2L, "MANAGEMENT");
    public static final Role DEVELOPER = new Role(3L, "DEVELOPER");
    public static final Role BLOCKED = new Role(4L, "BLOCKED");
    public static final Role PENDING = new Role(5L, "PENDING");

}