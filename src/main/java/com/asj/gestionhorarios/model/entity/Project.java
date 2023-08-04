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
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "project")
@SQLDelete(sql = "UPDATE project SET disabled=true WHERE project_id= ?")
@FilterDef(name = "deletedProjectFilter", parameters = @ParamDef(name = "isDisabled", type = "boolean"))
@Filter(name = "deletedProjectFilter", condition = "disabled = :isDisabled")
@EntityListeners(AuditingEntityListener.class)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long project_id;
    @Column(name = "hour_price", length = 15, nullable = false)
    private double hour_price;
    @Column(name = "name", length = 45, nullable = false, unique = true)
    private String name;
    @Column(name = "stack", length = 45)
    private String stack;
    @Column(name = "description")
    private String description;
    @Column(name = "hours_estimate")
    private Long hours_estimate;
    @Column(name = "end_estimate_date")
    private LocalDate end_estimate_date;
    @Column(name = "disabled", length = 15)
    private boolean disabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Person> people;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Sprint> sprints;

    public Project(double hour_price, String name, String stack, String description, Long hours_estimate, LocalDate end_estimate_date) {
        this.hour_price = hour_price;
        this.name = name;
        this.stack = stack;
        this.description = description;
        this.hours_estimate = hours_estimate;
        this.end_estimate_date = end_estimate_date;
    }

}
