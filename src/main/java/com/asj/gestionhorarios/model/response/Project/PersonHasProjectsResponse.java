package com.asj.gestionhorarios.model.response.Project;

import com.asj.gestionhorarios.model.entity.Project;
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
public class PersonHasProjectsResponse {
    private Long project_id;
    private String name;
    private Long hours_estimate;
    private String business_name;
    private String stack;

    public static PersonHasProjectsResponse toResponse(Project project) {
        return PersonHasProjectsResponse.builder()
                .project_id(project.getProject_id())
                .name(project.getName())
                .hours_estimate(project.getHours_estimate())
                .business_name(project.getClient().getBusiness_name())
                .stack(project.getStack())
                .build();
    }

    public static List<PersonHasProjectsResponse> toResponseList(List<Project> projects) {
        return projects.stream().map(PersonHasProjectsResponse::toResponse).collect(Collectors.toList());
    }
}
