package com.asj.gestionhorarios.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPersonUpdate {
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;

    private List<String> roleNames;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_job_relation;
}
