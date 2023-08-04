package com.asj.gestionhorarios.model.response.Client;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    public Long client_id;
    public String business_name;
    public LocalDate initial_date;
    public String address;

    public String email;

    public static ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .client_id(client.getClient_id())
                .business_name(client.getBusiness_name())
                .initial_date(client.getInitial_date())
                .email(client.getEmail())
                .address(client.getAddress())
                .build();
    }
}
