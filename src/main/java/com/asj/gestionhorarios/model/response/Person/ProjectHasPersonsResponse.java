package com.asj.gestionhorarios.model.response.Person;

import com.asj.gestionhorarios.model.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHasPersonsResponse {

    private String name;
    private String lastname;
    private String email;

    public static ProjectHasPersonsResponse toResponse(Person person) {
        return ProjectHasPersonsResponse.builder()
                .name(person.getName())
                .lastname(person.getLastname())
                .email(person.getEmail())
                .build();
    }

    public static List<ProjectHasPersonsResponse> toResponseList(List<Person> persons) {
        return persons.stream().map(ProjectHasPersonsResponse::toResponse).collect(Collectors.toList());
    }
}