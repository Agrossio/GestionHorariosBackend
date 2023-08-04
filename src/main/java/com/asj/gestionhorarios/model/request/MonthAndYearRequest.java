package com.asj.gestionhorarios.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthAndYearRequest {
    @NotNull(message = "Year can't be blank")
    private Integer year;

    @NotNull(message = "Month can't be blank")
    @Range(min = 1, max = 12, message = "The month must be a value between 1 and 12.")
    private Integer month;
}
