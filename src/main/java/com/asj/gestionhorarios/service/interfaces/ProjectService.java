package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.request.ProjectRequest;
import com.asj.gestionhorarios.model.request.ProjectUpdateRequest;
import com.asj.gestionhorarios.model.response.Project.ProjectResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    ProjectResponse save(ProjectRequest project);

    ProjectResponse findByProjectId(Long project_id);

    String delete(Long id_project) throws AccessDeniedException;

    ProjectResponse update(Long project_id, ProjectUpdateRequest pUpdate);

    List<ProjectResponse> findAllFilter(boolean isDisabled);

    Page<ProjectResponse> findFiltered(String name, String stack, LocalDate end_estimate_date, boolean disabled, Long project_id, Pageable pageable);


}
