package com.asj.gestionhorarios.model.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthId implements Serializable {
    private Integer year;
    private Integer month;
}