package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.entity.Sprint;
import com.asj.gestionhorarios.model.request.ClientRequest;
import com.asj.gestionhorarios.model.request.SprintRequest;
import com.asj.gestionhorarios.model.request.SprintUpdateRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.ClientService;
import com.asj.gestionhorarios.service.interfaces.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/sprint")
@RequiredArgsConstructor
public class SprintController {
    private final SprintService sprintService;
    @GetMapping("/{project_id}")
    public ResponseEntity<Response> findByProjectIdPaginated(@PathVariable Long project_id, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Sprints Found", sprintService.findAllByProjectIdPaginated(project_id, pageable)));
    }

    @GetMapping( "/details/{sprint_id}")
    public ResponseEntity<Response> findById(@PathVariable Long sprint_id){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Sprint found by Id", sprintService.findBySprintId(sprint_id)));
    }

    @PostMapping
    public ResponseEntity<Response> saveSprint(@Valid @RequestBody SprintRequest sprintRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Sprint created", sprintService.createSprint(sprintRequest)));
    }

    @DeleteMapping("{sprint_id}")
    public ResponseEntity<Response> deleteSprint(@PathVariable Long sprint_id) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Sprint deleted", sprintService.delete(sprint_id)));
    }

    @PutMapping("{sprint_id}")
    public ResponseEntity<Response> updateSprint(@PathVariable Long sprint_id, @Valid @RequestBody SprintUpdateRequest sprintUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response(Boolean.TRUE, "Sprint updated", sprintService.sprintUpdate(sprint_id, sprintUpdateRequest)));
    }

}
