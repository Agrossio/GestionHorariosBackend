package com.asj.gestionhorarios.model.request;

import com.asj.gestionhorarios.model.entity.Client;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {
    @NotBlank(message = "Name can't be blank")
    private String business_name;
    @NotBlank(message = "Email can't be blank")
    @Email(message = "Email must be a valid email")
    private String email;
    @NotBlank(message = "Address can't be blank")
    private String address;
    @NotNull(message = "Initial date can't be blank")
    @PastOrPresent(message = "Initial date can't be in the future")
    private LocalDate initial_date;


    public static Client toEntity(ClientRequest request){
        return Client.builder()
                .business_name(request.getBusiness_name())
                .email(request.getEmail())
                .initial_date(request.getInitial_date())
                .address(request.getAddress())
                .build();
    }
}
