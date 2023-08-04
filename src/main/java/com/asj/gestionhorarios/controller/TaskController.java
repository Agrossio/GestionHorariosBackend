package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.request.TaskRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @GetMapping("/{project_id}")
    public ResponseEntity<Response> findTaskByProject(@RequestParam(value = "isDisabled", required = false, defaultValue = "false") boolean isDisabled, @PathVariable Long project_id, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Tasks finded", service.findByProject(project_id, isDisabled, pageable)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> findFilteredTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String priorityName,
            @RequestParam(required = false) String statusName,
            @RequestParam(required = false) boolean disabled,
            Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Tasks found", service.findFiltered(title, projectId, email, priorityName, statusName, disabled, pageable)));

    }

    @GetMapping("/available/{project_id}")
    public ResponseEntity<Response> findAvailableTask(@RequestParam(value = "isDisabled", required = false, defaultValue = "false") boolean isDisabled, @PathVariable Long project_id, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Tasks finded", service.findAvailable(project_id, isDisabled, pageable)));

    }
    @PostMapping
    public ResponseEntity<Response> saveTask(@Valid @RequestBody TaskRequest task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Task created", service.save(task)));
    }
    @PutMapping("/{task_id}")
    public ResponseEntity<Response> updateTask(@Valid @RequestBody TaskRequest task,
                                               @PathVariable Long task_id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Boolean.TRUE, "Task updated", service.update(task, task_id)));
    }
    @PatchMapping("/add-dev/{email}/{task_id}")
    public ResponseEntity<Response> addDevToTask(@PathVariable String email,
                                                 @PathVariable Long task_id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Boolean.TRUE, "Task updated", service.addDev(task_id, email)));
    }
    @PatchMapping("/delete-dev/{email}/{task_id}")
    public ResponseEntity<Response> deleteDevToTask(@PathVariable String email,
                                                 @PathVariable Long task_id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Boolean.TRUE, "Task updated", service.deleteDev(task_id, email)));
    }
    @PatchMapping("/change-status/{status}/{task_id}")
    public ResponseEntity<Response> changeStatusToTask(@PathVariable Long task_id,
                                                       @PathVariable String status) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Boolean.TRUE, "Task updated", service.changeStatus(task_id, status)));
    }
    @DeleteMapping("/{task_id}")
    public ResponseEntity<Response> deleteTask(@PathVariable Long task_id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response(Boolean.TRUE, "Task deleted", service.delete(task_id)));
    }
}