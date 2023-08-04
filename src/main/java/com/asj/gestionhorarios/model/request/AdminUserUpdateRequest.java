package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserUpdateRequest {
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Lastname can't be blank")
    private String lastname;
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "CUIT/CUIL can't be blank")
    @Size(min = 4, message = "CUIT/CUIL must have more than 4 characters")
    private String cuil;
    @NotBlank(message = "Telephone can't be blank")
    private String tel;

    @NotNull(message = "Hours Jornal can't be null")
    @Positive(message = "Hours Jornal can't be negative")
    private Integer hours_journal;

    @NotNull(message = "Start Job Relation can´t be null")
    private LocalDate start_job_relation;
    @NotNull(message = "Role can't be null")
    private Set<String> roleNames;

    public static Person toEntity(AdminUserUpdateRequest userUpdateRequest) {
        return Person.builder()
                .name(userUpdateRequest.getName())
                .lastname(userUpdateRequest.getLastname())
                .email(userUpdateRequest.getEmail())
                .cuil(userUpdateRequest.getCuil())
                .tel(userUpdateRequest.getTel())
                .hours_journal(userUpdateRequest.getHours_journal())
                .start_job_relation(userUpdateRequest.getStart_job_relation())
                .build();

    }
    public AdminUserUpdateRequest (Person person, Set<Role> roles){
        this.name = person.getName();
        this.lastname = person.getLastname();
        this.email = person.getEmail();
        this.cuil = person.getCuil();
        this.tel = person.getTel();
        this.hours_journal = person.getHours_journal();
        this.start_job_relation = person.getStart_job_relation();
        Set<String> roleNames = roles.stream()
                .map(Role::getRole_name)
                .collect(Collectors.toSet());

        this.roleNames = roleNames;
    }
}