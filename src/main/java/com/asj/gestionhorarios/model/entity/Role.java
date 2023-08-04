package com.asj.gestionhorarios.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long role_id;
    @Column(name = "role_name", length = 15, nullable = false, unique = true)
    private String role_name;

    public Role(String role_name) {
        this.role_name = role_name;
    }
}