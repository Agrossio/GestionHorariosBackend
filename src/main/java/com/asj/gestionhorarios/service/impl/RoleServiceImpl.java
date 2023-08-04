package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.repository.RoleRepository;
import com.asj.gestionhorarios.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    @Override
    public List<String> findRoleNamesByEmail(String email) {
        return repository.findRoleNamesByEmail(email);
    }
}
