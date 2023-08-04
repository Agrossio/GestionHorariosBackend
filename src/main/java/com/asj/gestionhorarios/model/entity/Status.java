package com.asj.gestionhorarios.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long status_id;
    @Column(name = "status_name", length = 15, nullable = false, unique = true)
    private String status_name;
    public Status(String status_name) {
        this.status_name = status_name;
    }

}
