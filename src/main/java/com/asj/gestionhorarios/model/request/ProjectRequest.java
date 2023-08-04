package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    @NotNull(message = "Hour Price can't be null")
    private Double hour_price;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @NotBlank(message = "Stack can't be blank")
    private String stack;
    @NotBlank(message = "description can't be blank")
    private String description;

    @NotNull(message = "Estimated Hours can't be null")
    private Long hours_estimate;
    @NotNull(message = "End Estimate Date can't be null")
    private LocalDate end_estimate_date;
    @NotNull(message = "Client id can't be blank")
    private Long client_id;
    private List<Person> people;

    public static Project toEntity(ProjectRequest request){
        return Project.builder()
                .hour_price(request.getHour_price())
                .name(request.getName())
                .stack(request.getStack())
                .description(request.getDescription())
                .hours_estimate(request.getHours_estimate())
                .end_estimate_date(request.getEnd_estimate_date())
                .client(Client.builder().client_id(request.getClient_id()).build())
                .build();
    }
}
