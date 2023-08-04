package com.asj.gestionhorarios.model.response.Month;

import com.asj.gestionhorarios.model.entity.Month;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthResponse {

    public Integer year;

    public Integer month;

    public Integer working_days;


    public static MonthResponse toResponse(Month month){

        return MonthResponse.builder()
                .year(month.getYear())
                .month(month.getMonth())
                .working_days(month.getWorking_days())
                .build();

    }

}


