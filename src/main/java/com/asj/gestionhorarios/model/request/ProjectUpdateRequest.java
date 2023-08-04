package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateRequest {
    private Double hour_price;
    private String name;
    private String stack;
    private String description;
    private Long hours_estimate;
    @NotNull
    private LocalDate end_estimate_date;
    private Long client_id;
    private List<Person> people;
}
