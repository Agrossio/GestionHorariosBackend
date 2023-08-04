package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Task;
import com.asj.gestionhorarios.utils.PriorityFactory;
import com.asj.gestionhorarios.utils.StatusFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotBlank(message = "Title can't be blank")
    private String title;
    @NotBlank(message = "Description can't be blank")
    private String description;
    private double story_points;
    private LocalDate start_date;
    private LocalDate end_date;
    @NotNull(message = "Estimated Hours can't be null")
    private Long hours_estimate;
    private double worked_hours;
    @NotBlank(message = "Priority can't be blank")
    private String priority_name;
    @NotBlank(message = "Status can't be blank")
    private String status_name;
    @NotNull(message = "Project_id can't be null")
    private Long project_id;
    private String dev_email;
    private Long sprint_id;

    public static Task toEntity(TaskRequest request){
        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .story_points(request.getStory_points())
                .start_date(request.getStart_date())
                .end_date(request.getEnd_date())
                .hours_estimate(request.getHours_estimate())
                .worked_hours(request.getWorked_hours())
                .priority(PriorityFactory.newPriority(request.getPriority_name()))
                .status(StatusFactory.newStatus(request.getStatus_name()))
                .build();
    }
}