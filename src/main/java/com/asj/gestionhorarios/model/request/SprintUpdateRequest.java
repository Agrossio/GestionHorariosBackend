package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Sprint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintUpdateRequest {

    @NotNull(message = "Start date can´t be null, the provided format should be: yyyy-mm-dd")
    private LocalDate start_date;
    @NotNull(message = "End date can´t be null, the provided format should be: yyyy-mm-dd")
    private LocalDate end_date;

}
