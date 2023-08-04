package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.request.ClientRequest;
import com.asj.gestionhorarios.model.response.Client.ClientResponse;
import com.asj.gestionhorarios.model.response.Client.ClientResponseProject;
import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import com.asj.gestionhorarios.repository.ClientRepository;
import com.asj.gestionhorarios.repository.ProjectRepository;
import com.asj.gestionhorarios.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ClientResponse save(ClientRequest client) {
        if(clientExists(client.getEmail(), client.getBusiness_name())) throw new DataIntegrityViolationException("This client already exist");
        Client clientToSave = ClientRequest.toEntity(client);
        return ClientResponse.toResponse(clientRepository.save(clientToSave));
    }

    @Override
    public ClientResponseProject findByClientId(Long client_id) {
        Client client = findByClient(client_id);
        try {
            List<Project> projects = projectRepository.findProjectsByClientId(client_id);
            return new ClientResponseProject(client, projects);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String delete(Long client_id) throws AccessDeniedException {
        try{
            clientRepository.delete(findByClient(client_id));
            return "Delete OK";
        }
        catch (RuntimeException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public ClientResponse clientUpdate(Long client_id, ClientRequest cUpdate) {
        try{
            Client client = findByClient(client_id);
                client.setBusiness_name(cUpdate.getBusiness_name());
                client.setEmail(cUpdate.getEmail());
                client.setInitial_date(cUpdate.getInitial_date());
                client.setAddress(cUpdate.getAddress());
                return ClientResponse.toResponse(clientRepository.save(client));
            }
        catch(RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<ClientResponse> findAllFilter(boolean isDisabled) {
        try{
            return clientRepository.findAllByDisabled(isDisabled).stream()
                    .map(ClientResponse::toResponse)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Page<ClientResponse> findFiltered(String business_name, String email, LocalDate initial_date, String address, Pageable pageable) {

        Specification<Client> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (business_name != null && !business_name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("business_name"), "%" + business_name + "%"));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            if (initial_date != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("initial_date"), initial_date));
            }

            if (address != null && !address.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + address + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<Client> clientsPage = clientRepository.findAll(spec, pageable);

        List<ClientResponse> clientsResponse = clientsPage.stream()
                .map(ClientResponse::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(clientsResponse, pageable, clientsPage.getTotalElements());

    }

    public Client findByClient(Long client_id) {
        return clientRepository.findById(client_id).orElseThrow(()-> new NotFoundException("Client not found."));
    }

    public boolean clientExists(String email, String business_name) {
        return (clientRepository.clientExistByBusinessName(email, business_name) != null)? true: false;
    }
}
