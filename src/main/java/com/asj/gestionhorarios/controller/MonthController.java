package com.asj.gestionhorarios.controller;

import com.asj.gestionhorarios.model.request.MonthAndYearRequest;
import com.asj.gestionhorarios.model.request.MonthRequest;
import com.asj.gestionhorarios.model.response.Generic.Response;
import com.asj.gestionhorarios.service.interfaces.MonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/month")
@RequiredArgsConstructor
public class MonthController {

    private final MonthService service;

    @PostMapping
    public ResponseEntity<Response> saveOrUpdateMonth(@Valid @RequestBody List<MonthRequest> months){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Response(Boolean.TRUE, "Months created and updated", service.updateOrSave(months)));
    }

// Endpoint that was superseded by "saveOrUpdateMonth" and might be used in the future
//    @PostMapping
//    public ResponseEntity<Response> saveMonth(@Valid @RequestBody MonthRequest month){
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new Response(Boolean.TRUE, "Month created", service.save(month)));
//    }

    @PutMapping
    public ResponseEntity<Response> updateMonth(@Valid @RequestBody MonthRequest update) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new Response(Boolean.TRUE, "Month updated", service.monthUpdate(update)));
    }

    @GetMapping
    public ResponseEntity<Response> findWorkingDays(@Valid @RequestBody MonthAndYearRequest monthAndYear){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response(Boolean.TRUE, "Working days found by month and year", service.findWorkingDay(monthAndYear)));
    }
    @GetMapping("/all")
    public ResponseEntity<Response> findAll(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Response(Boolean.TRUE, "All months", service.findAll()));
    }
}
