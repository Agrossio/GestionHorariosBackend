package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.*;
import com.asj.gestionhorarios.model.request.TaskRequest;
import com.asj.gestionhorarios.model.response.Task.TaskResponse;
import com.asj.gestionhorarios.repository.PersonRepository;
import com.asj.gestionhorarios.repository.ProjectRepository;
import com.asj.gestionhorarios.repository.SprintRepository;
import com.asj.gestionhorarios.repository.TaskRepository;
import com.asj.gestionhorarios.service.interfaces.TaskService;
import com.asj.gestionhorarios.utils.PriorityFactory;
import com.asj.gestionhorarios.utils.StatusFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final SprintRepository sprintRepository;

    @Override
    public Page<TaskResponse> findByProject(Long project_id, boolean isDisabled, Pageable pageable) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        Page<Task> taskPage = repository.findAllByProjectAndDisabled(project, isDisabled, pageable);
        List<TaskResponse> taskResponses = taskPage.getContent().stream()
                .map(TaskResponse::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(taskResponses, pageable, taskPage.getTotalElements());
    }

    @Override
    public Page<TaskResponse> findFiltered(String title, Long projectId, String email, String priority_name, String status_name, boolean disabled, Pageable pageable) {
        Specification<Task> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // TODO: use a Switch Case:
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (projectId != null) {
                Join<Task, Project> projectJoin = root.join("project");
                predicates.add(criteriaBuilder.equal(projectJoin.get("project_id"), projectId));
            }
            if (email != null && !email.isEmpty()) {
                Join<Task, Person> personJoin = root.join("person");
                predicates.add(criteriaBuilder.equal(personJoin.get("email"), email));
            }
            if (priority_name != null && !priority_name.isEmpty()) {
                Join<Task, Priority> priorityJoin = root.join("priority");
                predicates.add(criteriaBuilder.equal(priorityJoin.get("priority_name"), priority_name));
            }
            if (status_name != null && !status_name.isEmpty()) {
                Join<Task, Status> priorityJoin = root.join("status");
                predicates.add(criteriaBuilder.equal(priorityJoin.get("status_name"), status_name));
            }
            predicates.add(criteriaBuilder.equal(root.get("disabled"), disabled));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<Task> tasksPage = repository.findAll(spec, pageable);
        List<TaskResponse> tasksResponse = tasksPage.stream()
                .map(TaskResponse::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(tasksResponse, pageable, tasksPage.getTotalElements());
    }

    @Override
    public Page<TaskResponse> findAvailable(Long project_id, boolean isDisabled, Pageable pageable) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        Page<Task> taskPage = repository.findAllByProjectAndDisabledAndPersonIsNull(project, isDisabled, pageable);
        List<TaskResponse> taskResponses = taskPage.getContent().stream()
                .map(TaskResponse::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(taskResponses, pageable, taskPage.getTotalElements());
    }

    @Override
    public TaskResponse save(TaskRequest task) {
        Task taskToCreate = TaskRequest.toEntity(task);
        if (task.getDev_email() != null) taskToCreate.setPerson(personRepository.findByEmail(task.getDev_email())
                .orElseThrow(() -> new NotFoundException("Person not found")));
        if (task.getSprint_id() != null) taskToCreate.setSprint(sprintRepository.findById(task.getSprint_id())
                .orElseThrow(() -> new NotFoundException("Sprint not found")));
        taskToCreate.setProject(projectRepository.findById(task.getProject_id())
                .orElseThrow(() -> new NotFoundException("Project not found")));
        return TaskResponse.toResponse(repository.save(taskToCreate));
    }

    @Override
    public TaskResponse update(TaskRequest task, Long task_id) {
        Task taskToUpdate = repository.findById(task_id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStory_points(task.getStory_points());
        taskToUpdate.setStart_date(task.getStart_date());
        taskToUpdate.setEnd_date(task.getEnd_date());
        taskToUpdate.setHours_estimate(task.getHours_estimate());
        taskToUpdate.setWorked_hours(task.getWorked_hours());
        taskToUpdate.setPriority(PriorityFactory.newPriority(task.getPriority_name()));
        taskToUpdate.setStatus(StatusFactory.newStatus(task.getStatus_name()));
        if (task.getDev_email() != null) taskToUpdate.setPerson(personRepository.findByEmail(task.getDev_email())
                .orElseThrow(() -> new NotFoundException("Person not found")));
        if (task.getSprint_id() != null) taskToUpdate.setSprint(sprintRepository.findById(task.getSprint_id())
                .orElseThrow(() -> new NotFoundException("Sprint not found")));
        return TaskResponse.toResponse(repository.save(taskToUpdate));
    }

    @Override
    public TaskResponse addDev(Long task_id, String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
        if (!authenticatedEmail.equals(email) && !isAdmin) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this task.");
        }
        Task task = repository.findById(task_id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        Long project_id = task.getProject().getProject_id();
        List<Person> listPerson = projectRepository.findById(project_id).get().getPeople();
        Person person = listPerson.stream()
                .filter(p -> email.equals(p.getEmail()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Person not found on project"));
        task.setPerson(person);
        return TaskResponse.toResponse(repository.save(task));
    }

    @Override
    public TaskResponse deleteDev(Long task_id, String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        if (!authenticatedEmail.equals(email)) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this task.");
        }
        Task task = repository.findById(task_id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        task.setPerson(null);
        return TaskResponse.toResponse(repository.save(task));
    }

    @Override
    public TaskResponse changeStatus(Long task_id, String status) {
        Task task = repository.findById(task_id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();
        if (task.getPerson() == null) {
            throw new org.springframework.security.access.AccessDeniedException("you have to assign yourself to this task to change its status.");
        } else if (!authenticatedEmail.equals(task.getPerson().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("You are not authorized to update this task.");
        }
        task.setStatus(StatusFactory.newStatus(status));
        return TaskResponse.toResponse(repository.save(task));
    }

    @Override
    public String delete(Long task_id) {
        repository.delete(repository.findById(task_id)
                .orElseThrow(() -> new NotFoundException("Task not found")));
        return "Task deleted";
    }
}