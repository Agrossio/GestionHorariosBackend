package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.entity.Person;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.request.ProjectRequest;
import com.asj.gestionhorarios.model.request.ProjectUpdateRequest;
import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import com.asj.gestionhorarios.repository.ClientRepository;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.repository.ProjectRepository;
import com.asj.gestionhorarios.service.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ClientRepository clientRepository;
    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    @Override
    public ProjectResponse save(ProjectRequest project) {
        if (projectRepository.existsByName(project.getName()))
            throw new DataIntegrityViolationException("This project already exist");
        Client client = clientExists(project.getClient_id());
        Project projectToSave = ProjectRequest.toEntity(project);
        projectToSave.setClient(client);
        if (project.getPeople() != null) {
            List<Person> peopleInProject = new ArrayList<>();
            for (Person person : project.getPeople()) {
                Person personFound = personExists(person.getEmail());
                peopleInProject.add(personFound);
            }
            projectToSave.setPeople(peopleInProject);
        } else {
            projectToSave.setPeople(Arrays.asList());
        }
        projectToSave.setSprints(Arrays.asList());
        return ProjectResponse.toResponse(projectRepository.save(projectToSave));
    }

    @Override
    public ProjectResponse findByProjectId(Long project_id) {
        Project foundProject = (projectRepository.findById(project_id)).orElseThrow(() -> new NotFoundException("Project not found."));
        return ProjectResponse.toResponse(foundProject);
    }

    @Override
    public String delete(Long project_id) throws AccessDeniedException {
        projectRepository.delete(findById(project_id));
        return "Delete OK";
    }

    @Override
    public ProjectResponse update(Long project_id, ProjectUpdateRequest pUpdate) {
        Project foundProject = (projectRepository.findProjectWithPeople(project_id)).orElseThrow(() -> new NotFoundException("Project not found."));
        Project updatedProject = foundProject;
        if (pUpdate.getClient_id() != null) {
            Client auxClient = new Client();
            auxClient.setClient_id(pUpdate.getClient_id());
            foundProject.setClient(auxClient);
        }
        if (!StringUtils.isBlank(pUpdate.getName())) updatedProject.setName(pUpdate.getName());
        if (!StringUtils.isBlank(pUpdate.getStack())) updatedProject.setStack(pUpdate.getStack());
        if (!StringUtils.isBlank(pUpdate.getDescription())) updatedProject.setDescription(pUpdate.getDescription());
        if (pUpdate.getHours_estimate() != null) updatedProject.setHours_estimate(pUpdate.getHours_estimate());
        if (pUpdate.getHour_price() != null) updatedProject.setHour_price(pUpdate.getHour_price());

        updatedProject.setEnd_estimate_date(pUpdate.getEnd_estimate_date());
        List<Person> peopleInProject = new ArrayList<>();
        if(pUpdate.getPeople()!=null) {
            for (Person person : pUpdate.getPeople()) {
                Person personFound = personExists(person.getEmail());
                if (!peopleInProject.contains(personFound)) peopleInProject.add(personFound);
            }
            updatedProject.setPeople(peopleInProject);
        }
        return ProjectResponse.toResponse(projectRepository.save(updatedProject));
    }

    @Override
    public List<ProjectResponse> findAllFilter(boolean isDeleted) {
        return projectRepository.findAllByDisabled(isDeleted).stream()
                .map(ProjectResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProjectResponse> findFiltered(String name, String stack, LocalDate end_estimate_date, boolean disabled, Long project_id, Pageable pageable) {
        Specification<Project> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }

            if (stack != null && !stack.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("stack"), "%" + stack + "%"));
            }

            if (end_estimate_date != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("end_estimate_date"), end_estimate_date));
            }

            if (project_id != null) {
                predicates.add(criteriaBuilder.equal(root.get("project_id"), project_id));
            }

            predicates.add(criteriaBuilder.equal(root.get("disabled"), disabled));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        Page<Project> projectPage = projectRepository.findAll(spec, pageable);

        List<ProjectResponse> projectResponses = projectPage.stream()
                .map(ProjectResponse::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(projectResponses, pageable, projectPage.getTotalElements());
    }


    public Project findById(Long project_id) {
        return projectRepository.findById(project_id).orElseThrow(() -> new NotFoundException("Project not found"));
    }

    public Client clientExists(Long client_id) {
        return clientRepository.findById(client_id).orElseThrow(() -> new NotFoundException("Client not found."));
    }

    public Person personExists(String personMail) {
        return personRepository.findByEmail(personMail).orElseThrow(() -> new NotFoundException("Person not found."));
    }
}
