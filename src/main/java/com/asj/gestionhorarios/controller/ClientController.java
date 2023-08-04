package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.entity.Client;
import com.asj.gestionhorarios.model.request.ClientRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.ClientService;
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
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @GetMapping( "{client_id}")
    public ResponseEntity<Response> findById(@PathVariable Long client_id){
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Client found by Id", clientService.findByClientId(client_id)));
    }


    @PostMapping
    public ResponseEntity<Response> saveClient(@Valid @RequestBody ClientRequest client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(Boolean.TRUE, "Client created", clientService.save(client)));
    }

    @DeleteMapping("{client_id}")
    public ResponseEntity<Response> deleteClient(@PathVariable Long client_id) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE, "Client deleted", clientService.delete(client_id)));
    }
    @PutMapping("{client_id}")
    public ResponseEntity<Response> updateClient(@PathVariable Long client_id, @Valid @RequestBody ClientRequest cUpdate) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Client updated", clientService.clientUpdate(client_id, cUpdate)));
    }

    @GetMapping
    public ResponseEntity<Response> findAll(@RequestParam(value = "isDeleted", required = false, defaultValue = "false") boolean isDeleted) {
        log.warn("This endpoint is deprecated and it will be deleted in future versions, use /api/v1/client/filter with the required filters instead");
        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Found clients: This endpoint is deprecated and it will be deleted in future versions, use /api/v1/client/filter with the required filters instead", clientService.findAllFilter(isDeleted)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Response> findFilteredClients(
            @RequestParam(required = false) String business_name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initial_date,
            @RequestParam(required = false) String address,
            Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(new Response(Boolean.TRUE,  "Found clients", clientService.findFiltered(business_name, email, initial_date, address, pageable)));

    }

}
