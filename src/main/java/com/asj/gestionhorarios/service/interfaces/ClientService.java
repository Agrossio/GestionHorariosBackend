package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.request.ClientRequest;
import com.asj.gestionhorarios.model.response.Client.ClientResponse;
import com.asj.gestionhorarios.model.response.Client.ClientResponseProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface ClientService {

    ClientResponse save(ClientRequest client);

    ClientResponseProject findByClientId(Long clientId);

    String delete(Long clientId) throws AccessDeniedException;
    ClientResponse clientUpdate (Long clientId, ClientRequest cUpdate);

    List<ClientResponse> findAllFilter(boolean isDeleted);

    Page<ClientResponse> findFiltered(String business_name, String email, LocalDate initial_date, String address, Pageable pageable);


}
