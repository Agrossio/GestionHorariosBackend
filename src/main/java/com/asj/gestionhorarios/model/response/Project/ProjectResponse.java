package com.asj.gestionhorarios.model.response.Project;

import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.response.Person.ProjectHasPersonsResponse;
import com.asj.gestionhorarios.model.response.Sprint.SprintResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Long project_id;
    private double hour_price;
    private String name;
    private String stack;
    private String description;
    private Long hours_estimate;
    private LocalDate end_estimate_date;
    private String business_name;
    private List<ProjectHasPersonsResponse> people;
    private List<SprintResponse> sprints;

    // no enviar los sprints sino un numero con la cantidad total de sprints que tiene el proyecto
    // agregar el sprint actual del tipo Sprint

    public static ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .project_id(project.getProject_id())
                .hour_price(project.getHour_price())
                .name(project.getName())
                .stack(project.getStack())
                .description(project.getDescription())
                .hours_estimate(project.getHours_estimate())
                .end_estimate_date(project.getEnd_estimate_date())
                .business_name(project.getClient().getBusiness_name())
                .people(ProjectHasPersonsResponse.toResponseList(project.getPeople()))
                .sprints(toListSprintResponse(project.getSprints()))
                .build();
    }

    public static List<SprintResponse> toListSprintResponse(List<Sprint> sprints){
        return sprints.stream().map(SprintResponse::toResponse).collect(Collectors.toList());
    }

}
