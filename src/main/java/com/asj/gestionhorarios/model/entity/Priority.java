package com.asj.gestionhorarios.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "priority")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Long priority_id;
    @Column(name = "priority_name", length = 15, nullable = false, unique = true)
    private String priority_name;

    public Priority(String priority_name) { this.priority_name = priority_name; }

}
