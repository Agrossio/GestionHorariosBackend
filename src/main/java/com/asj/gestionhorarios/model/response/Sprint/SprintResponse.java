package com.asj.gestionhorarios.model.response.Sprint;

import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Sprint;

import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponse {
    public Long sprint_id;
    public Integer sprint_number;
    public LocalDate start_date;
    public LocalDate end_date;
    public Long project_id;


    public static SprintResponse toResponse(Sprint sprint) {
        return SprintResponse.builder()
                .sprint_id(sprint.getSprint_id())
                .sprint_number(sprint.getSprint_number())
                .start_date(sprint.getStart_date())
                .end_date(sprint.getEnd_date())
                .project_id(sprint.getProject_id())
                .build();
    }
}
