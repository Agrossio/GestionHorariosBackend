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
@Table(name= "client")
@SQLDelete(sql = "UPDATE client SET disabled=true WHERE client_id= ?")
@FilterDef(name = "deletedClientFilter", parameters = @ParamDef(name = "isDisabled", type = "boolean"))
@Filter(name = "deletedClientFilter", condition = "disabled = :isDisabled")
@EntityListeners(AuditingEntityListener.class)
public class Client extends AuditingEntityListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long client_id;
    @Column(name = "business_name", length = 45, nullable = false, unique = true)
    private String business_name;
    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;
    @Column(name = "initial_date", nullable = false)
    private LocalDate initial_date;
    @Column(name = "address", length = 255)
    private String address;
    @Column(name = "disabled", length = 15)
    private boolean disabled;

    public Client(String business_name, String email, LocalDate initial_date, String address) {
        this.business_name = business_name;
        this.email = email;
        this.initial_date = initial_date;
        this.address = address;
    }
}
