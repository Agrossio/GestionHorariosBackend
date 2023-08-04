package com.asj.gestionhorarios.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "sprint")
//@SQLDelete(sql = "UPDATE sprint SET disabled=true WHERE sprint_id= ?")
//@FilterDef(name = "deletedSprintFilter", parameters = @ParamDef(name = "isDisabled", type = "boolean"))
//@Filter(name = "deletedSprintFilter", condition = "disabled = :isDisabled")
@EntityListeners(AuditingEntityListener.class)
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long sprint_id;
    @Column(name = "sprint_number", length = 15, nullable = false)
    private Integer sprint_number;
    @Column(name = "start_date")
    private LocalDate start_date;
    @Column(name = "end_date")
    private LocalDate end_date;
//    @Column(name = "disabled", length = 15)
//    private boolean disabled;
    @Column(name = "project_id")
    private Long project_id;

    public Sprint(Integer sprint_number, LocalDate start_date, LocalDate end_date, Long project_id) {
        this.sprint_number = sprint_number;
        this.start_date = start_date;
        this.end_date = end_date;
        this.project_id = project_id;
    }
}
