package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.request.ProjectRequest;
import com.asj.gestionhorarios.model.request.ProjectUpdateRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping( "{project_id}")
    public ResponseEntity<Response> findByProjectId(@PathVariable Long project_id){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Project found by Id", projectService.findByProjectId(project_id)));
    }

    @PostMapping
    public ResponseEntity<Response> saveProject(@Valid @RequestBody ProjectRequest project) throws RuntimeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Project created", projectService.save(project)));
    }

    @DeleteMapping("{project_id}")
    public ResponseEntity<Response> deleteProject(@PathVariable Long project_id) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Project deleted", projectService.delete(project_id)));
    }

    @PutMapping("{project_id}")
    public ResponseEntity<Response> updateProject(@PathVariable Long project_id, @Valid @RequestBody ProjectUpdateRequest pUpdate) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Project updated", projectService.update(project_id, pUpdate)));
    }

    @GetMapping
    public ResponseEntity<Response> findAll(@RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Found projects: This endpoint is deprecated and it will be deleted in future versions, use /api/v1/project/filter with the required filters instead", projectService.findAllFilter(isDeleted)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> findFilteredProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String stack,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_estimate_date,
            @RequestParam(required = false) boolean disabled,
            @RequestParam(required = false) Long projectId,
            Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Found projects", projectService.findFiltered(name, stack, end_estimate_date, disabled, projectId, pageable)));

    }

}
