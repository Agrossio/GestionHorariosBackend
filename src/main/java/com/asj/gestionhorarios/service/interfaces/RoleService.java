package com.asj.gestionhorarios.service.interfaces;

import java.util.List;

public interface RoleService {
    List<String> findRoleNamesByEmail(String email);
}
