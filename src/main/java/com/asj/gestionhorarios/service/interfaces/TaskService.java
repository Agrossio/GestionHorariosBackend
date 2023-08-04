package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.entity.Task;
import com.asj.gestionhorarios.model.request.TaskRequest;
import com.asj.gestionhorarios.model.response.Task.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Page<TaskResponse> findByProject(Long project_id, boolean isDisabled, Pageable pageable);
    Page<TaskResponse> findAvailable(Long project_id, boolean isDisabled, Pageable pageable);
    TaskResponse save(TaskRequest task);
    TaskResponse update(TaskRequest task, Long task_id);
    TaskResponse addDev(Long task_id, String email);
    TaskResponse deleteDev(Long task_id, String email);
    TaskResponse changeStatus(Long task_id, String status);
    String delete(Long task_id);
    Page<TaskResponse> findFiltered(String title, Long projectId, String email, String priority_name, String status_name, boolean disabled, Pageable pageable);
}
