package com.asj.gestionhorarios.model.entity;

import javax.persistence.Id;
import com.asj.gestionhorarios.model.ids.MonthId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "month")
@IdClass(MonthId.class)
public class Month implements Serializable {
    @Id
    @Column(name= "year")
    private Integer year;
    @Id
    @Column(name = "month")
    private Integer month;

    @Column(name = "working_days")
    private Integer working_days;
}
