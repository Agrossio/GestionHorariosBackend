package com.asj.gestionhorarios.model.entity;

import com.asj.gestionhorarios.utils.AuditorFieldsEntity;
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
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "person")
@SQLDelete(sql = "UPDATE person SET disabled=true WHERE email = ?")
@FilterDef(name = "deletedPersonFilter", parameters = @ParamDef(name = "isDisabled", type = "boolean"))
@Filter(name = "deletedPersonFilter", condition = "disabled = :isDisabled")
@EntityListeners(AuditingEntityListener.class)
public class Person extends AuditorFieldsEntity {
    @Id
    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    @Column(name = "name", length = 45, nullable = false)
    private String name;
    @Column(name = "lastname", length = 45, nullable = false)
    private String lastname;
    @Column(name = "tel", length = 255)
    private String tel;
    @Column(name = "cuil", length = 15, nullable = false)
    private String cuil;
    @Column(name = "hours_journal")
    private Integer hours_journal;
    @Column(name = "start_job_relation")
    private LocalDate start_job_relation;
    @Column(name = "image")
    private byte[] image;
    @Column(name = "disabled", length = 15)
    private boolean disabled;
    @ManyToMany
    private Set<Role> roles;

    public Person(String email, String password, String name, String lastname, String tel, String cuil, Integer hours_journal, LocalDate start_job_relation) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
        this.tel = tel;
        this.cuil = cuil;
        this.hours_journal = hours_journal;
        this.start_job_relation = start_job_relation;
    }
}