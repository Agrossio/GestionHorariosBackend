package com.asj.gestionhorarios.model.response.Person;

import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import com.asj.gestionhorarios.model.response.Project.PersonHasProjectsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private String name;
    private String lastname;
    private String cuil;
    private String tel;
    private String email;
    private Integer hours_journal;
    private boolean disabled;
    private LocalDate start_job_relation;
    private Set<Role> roles;
    private List<PersonHasProjectsResponse> projects;

    public static PersonResponse toResponse(Person person) {
        return PersonResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .cuil(person.getCuil())
                .email(person.getEmail())
                .hours_journal(person.getHours_journal())
                .disabled(person.isDisabled())
                .tel(person.getTel())
                .start_job_relation(person.getStart_job_relation())
                .roles(person.getRoles())
                .build();
    }
}
