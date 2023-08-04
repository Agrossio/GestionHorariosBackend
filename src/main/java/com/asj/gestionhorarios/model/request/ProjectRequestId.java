package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestId {

    @NotBlank(message = "Project Id can't be blank")
    @NotNull(message = "Project Id can't be null")
    @NotEmpty(message = "Project Id can't be empty")
    private Long project_id;
    @NotBlank(message = "Hour Price can't be blank")
    @NotNull(message = "Hour Price can't be null")
    @NotEmpty(message = "Hour Price can't be empty")
    private Double hour_price;
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotBlank(message = "Stack can't be blank")
    @NotNull(message = "Stack can't be null")
    @NotEmpty(message = "Stack can't be empty")
    private String stack;
    @NotBlank(message = "description can't be blank")
    @NotNull(message = "description can't be null")
    @NotEmpty(message = "description can't be empty")
    private String description;
    @NotBlank(message = "Estimated Hours can't be blank")
    @NotNull(message = "Estimated Hours can't be null")
    @NotEmpty(message = "Estimated Hours can't be empty")
    private Long hours_estimate;
    @NotNull(message = "End Estimate Date can't be null")
    @NotEmpty(message = "End Estimate Date can't be empty")
    private LocalDate end_estimate_date;

    public static Project toEntityId(ProjectRequestId requestId){
        return Project.builder()
                .project_id(requestId.getProject_id())
                .hour_price(requestId.getHour_price())
                .name(requestId.getName())
                .stack(requestId.getStack())
                .description(requestId.getDescription())
                .hours_estimate(requestId.getHours_estimate())
                .end_estimate_date(requestId.getEnd_estimate_date())
                .build();
    }
}
