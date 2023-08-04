package com.asj.gestionhorarios.security.service;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.enums.RoleTypes;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.service.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Component
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;
    private final RoleServiceImpl roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new NotFoundException("Person not found"));
        List<String> roles = person.getRoles()
                .stream()
                .map(Role::getRole_name)
                .collect(Collectors.toList());
        String restringed =
                roles.contains(RoleTypes.BLOCKED.getRole_name()) ? RoleTypes.BLOCKED.getRole_name() :
                roles.contains(RoleTypes.PENDING.getRole_name()) ? RoleTypes.PENDING.getRole_name() :
                        null;
        List<SimpleGrantedAuthority> authorities = restringed != null ?
                Collections.singletonList(new SimpleGrantedAuthority(restringed)) :
                roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return new User(person.getEmail(), person.getPassword(), authorities);
    }
}