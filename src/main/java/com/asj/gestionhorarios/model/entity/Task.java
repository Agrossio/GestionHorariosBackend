package com.asj.gestionhorarios.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "task")
@SQLDelete(sql = "UPDATE task SET disabled=true WHERE task_id = ?")
@FilterDef(name = "deletedTaskFilter", parameters = @ParamDef(name = "isDisabled", type = "boolean"))
@Filter(name = "deletedTaskFilter", condition = "disabled = :isDisabled")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long task_id;
    @Column(name = "title", length = 40, nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "story_points")
    private double story_points;
    @Column(name = "start_date")
    private LocalDate start_date;
    @Column(name = "end_date")
    private LocalDate end_date;
    @Column(name = "hours_estimate")
    private Long hours_estimate;
    @Column(name = "worked_hours")
    private double worked_hours;
    @Column(name = "disabled", length = 15)
    private boolean disabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "person_id")
    private Person person;
    @OneToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;
    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;

    public Task(String title, String description, double story_points, LocalDate start_date, LocalDate end_date, Long hours_estimate, double worked_hours) {
        this.title = title;
        this.description = description;
        this.story_points = story_points;
        this.start_date = start_date;
        this.end_date = end_date;
        this.hours_estimate = hours_estimate;
        this.worked_hours = worked_hours;
    }
}
