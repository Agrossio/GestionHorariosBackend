package com.asj.gestionhorarios.model.response.Client;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ClientResponseProject{
    public Long client_id;
    public String business_name;
    public LocalDate initial_date;
    public String address;

    public String email;
    public List<ProjectResponse> projects;

    public static ClientResponseProject toResponse(Client client) {
        return ClientResponseProject.builder()
                .client_id(client.getClient_id())
                .business_name(client.getBusiness_name())
                .initial_date(client.getInitial_date())
                .email(client.getEmail())
                .address(client.getAddress())
                .build();
    }

    public ClientResponseProject(Client client, List<Project> projects) {
        this.client_id = client.getClient_id();
        this.business_name = client.getBusiness_name();
        this.initial_date = client.getInitial_date();
        this.address = client.getAddress();
        this.email = client.getEmail();
        this.projects = projects.stream()
                .map(project -> ProjectResponse.toResponse(project))
                .collect(Collectors.toList());
    }


}
