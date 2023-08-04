package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.entity.Task;
import com.asj.gestionhorarios.model.request.SprintRequest;
import com.asj.gestionhorarios.model.request.SprintUpdateRequest;
import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import com.asj.gestionhorarios.model.response.Sprint.SprintResponse;
import com.asj.gestionhorarios.model.response.Task.TaskResponse;
import com.asj.gestionhorarios.repository.ProjectRepository;
import com.asj.gestionhorarios.repository.SprintRepository;
import com.asj.gestionhorarios.repository.TaskRepository;
import com.asj.gestionhorarios.service.interfaces.ProjectService;
import com.asj.gestionhorarios.service.interfaces.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintServiceImpl implements SprintService {
    private final SprintRepository sprintRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    @Override
    public SprintResponse findBySprintId(Long sprintId) {

        Sprint foundSprint = sprintRepository.findById(sprintId).orElseThrow(()-> new NotFoundException("Sprint Not Found"));

        try {
            return SprintResponse.toResponse(foundSprint);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
    @Override
    public Page<SprintResponse> findAllByProjectIdPaginated(Long project_id, Pageable pageable) {

        if(!projectRepository.existsByProject_id(project_id)) throw new NotFoundException("Project doesn't exists.");

        try{

            Page<Sprint> sprintsPage = this.sprintRepository.findAllByProjectIdPaginated(project_id, pageable);

            List<SprintResponse> sprintResponses = sprintsPage.getContent().stream()
                    .map(SprintResponse::toResponse)
                    .collect(Collectors.toList());

            return new PageImpl<>(sprintResponses, pageable, sprintsPage.getTotalElements());

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    @Override
    public SprintResponse createSprint(SprintRequest sprintRequest) {

        Project foundProject = projectRepository.findById(sprintRequest.getProject_id()).orElseThrow(() -> new NotFoundException("Project doesn't exists."));

        if(isRepeated(foundProject.getSprints(), sprintRequest.getSprint_number())) throw new DataIntegrityViolationException("This sprint number is already in use in this project");

        try {

            foundProject.getSprints().add(SprintRequest.toEntity(sprintRequest));
            Project savedProject = projectRepository.save(foundProject);

            return SprintResponse.toResponse(savedProject.getSprints().stream()
                    .filter(sprint -> sprint.getSprint_number() == sprintRequest.getSprint_number())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException()));

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean isRepeated(List<Sprint> sprints, Integer sprintNumber){
        Boolean isRepeated = false;

        for (Sprint sprint : sprints){
            if(sprint.getSprint_number() == sprintNumber) {
                isRepeated = true;
                break;
            }
        }
        return isRepeated;
    }

    @Override
    public String delete(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).orElseThrow(()-> new NotFoundException("Sprint doesn't exists."));
        List<Task> tasks = taskRepository.findAllBySprint(sprint);
        Project project = projectRepository.findById(sprint.getProject_id()).get();
        try {

            for (Task task : tasks) {
                task.setSprint(null);
            }

            List<Sprint> sprints = project.getSprints();
            List<Sprint> sprintsToSave = sprints.stream()
                    .filter((sp)-> sp.getSprint_id() != sprint.getSprint_id())
                    .collect(Collectors.toList());

            project.setSprints(sprintsToSave);
            projectRepository.save(project);
            taskRepository.saveAll(tasks);
            sprintRepository.delete(sprint);
            return "Sprint deleted";
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public SprintResponse sprintUpdate(Long sprintId, SprintUpdateRequest sprintUpdateRequest) {
        Sprint foundSprint = sprintRepository.findById(sprintId).orElseThrow(()-> new NotFoundException("Sprint doesn't exists."));
        try {
            foundSprint.setStart_date(sprintUpdateRequest.getStart_date());
            foundSprint.setEnd_date(sprintUpdateRequest.getEnd_date());
            return SprintResponse.toResponse(sprintRepository.save(foundSprint));
        } catch (PersistenceException e) {
            throw new PersistenceException(e.getMessage());
        }
    }



}
