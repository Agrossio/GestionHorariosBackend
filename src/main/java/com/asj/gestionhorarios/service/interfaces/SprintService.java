package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.request.SprintRequest;
import com.asj.gestionhorarios.model.request.SprintUpdateRequest;
import com.asj.gestionhorarios.model.response.Sprint.SprintResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SprintService {


    SprintResponse findBySprintId(Long sprintId);
//    List<SprintResponse> findAllByProjectId(Long project_id, boolean isDisabled);
    SprintResponse createSprint(SprintRequest sprintRequest);
    String delete(Long sprintId);
    SprintResponse sprintUpdate(Long sprintId, SprintUpdateRequest sprintUpdateRequest);
    Page<SprintResponse> findAllByProjectIdPaginated(Long project_id, Pageable pageable);
}
