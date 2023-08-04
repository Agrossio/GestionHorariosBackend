package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Sprint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintRequest {

    @NotNull(message = "Sprint Number can't be null")
    private Integer sprint_number;
    @NotNull(message = "Start Date can't be null")
    private LocalDate start_date;
    @NotNull(message = "End Date can't be null")
    private LocalDate end_date;
    @NotNull(message = "Project id can't be null")
    private Long project_id;
    
    public static Sprint toEntity(SprintRequest request){
        return Sprint.builder()
                .sprint_number(request.getSprint_number())
                .start_date(request.getStart_date())
                .end_date(request.getEnd_date())
                .project_id(request.getProject_id())
                .build();
    }
}
