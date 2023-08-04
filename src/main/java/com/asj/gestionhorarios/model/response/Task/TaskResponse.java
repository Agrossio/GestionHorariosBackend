package com.asj.gestionhorarios.model.response.Task;

import com.asj.gestionhorarios.model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    public Long task_id;
    public String title;
    public String description;
    public double story_points;
    public LocalDate start_date;
    public LocalDate end_date;
    public  Long hours_estimate;
    public double worked_hours;
    public Long project_id;
    public String priority_name;
    public String status_name;
    public String dev_email;
    public Integer sprint_number;

    public static TaskResponse toResponse(Task task) {
        TaskResponse.TaskResponseBuilder builder = TaskResponse.builder()
                .task_id(task.getTask_id())
                .title(task.getTitle())
                .description(task.getDescription())
                .story_points(task.getStory_points())
                .start_date(task.getStart_date())
                .end_date(task.getEnd_date())
                .hours_estimate(task.getHours_estimate())
                .worked_hours(task.getWorked_hours())
                .project_id(task.getProject().getProject_id())
                .priority_name(task.getPriority().getPriority_name())
                .status_name(task.getStatus().getStatus_name());

        if (task.getSprint() != null) {
            builder.sprint_number(task.getSprint().getSprint_number());
        }
        if(task.getPerson() != null) {
            builder.dev_email(task.getPerson().getEmail());
        }

        return builder.build();
    }
}