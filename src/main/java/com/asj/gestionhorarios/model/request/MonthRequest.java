package com.asj.gestionhorarios.model.request;


import com.asj.gestionhorarios.model.entity.Month;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthRequest {
    @NotNull(message = "Year can't be blank")
    private Integer year;

    @NotNull(message = "Month can't be blank")
    @Range(min = 1, max = 12, message = "The month must be a value between 1 and 12.")
    private Integer month;

    @NotNull(message = "Working days can't be blank")
    private Integer working_days;

    public static Month toEntity(MonthRequest request){
        return Month.builder()
                .year(request.getYear())
                .month(request.getMonth())
                .working_days(request.getWorking_days())
                .build();
    }

}
