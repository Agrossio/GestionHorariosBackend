package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Month;
import com.asj.gestionhorarios.model.request.MonthAndYearRequest;
import com.asj.gestionhorarios.model.request.MonthRequest;
import com.asj.gestionhorarios.model.response.Month.MonthResponse;
import com.asj.gestionhorarios.repository.MonthRepository;
import com.asj.gestionhorarios.service.interfaces.MonthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthServiceImpl implements MonthService {
    Month updatedMonth = new Month();
    private final MonthRepository repository;

    @Override
    public MonthResponse save(MonthRequest month){
        try {
            Month monthToSave = MonthRequest.toEntity(month);
            return MonthResponse.toResponse(repository.save(monthToSave));
        } catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public MonthResponse findWorkingDay(MonthAndYearRequest monthAndYear) {
        Month foundMonth = (repository.findByYearAndMonth(monthAndYear.getYear(),monthAndYear.getMonth())).orElseThrow(() -> new NotFoundException("Month not found."));
        try{
            return MonthResponse.toResponse(foundMonth);
        }
        catch (RuntimeException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MonthResponse> updateOrSave(List<MonthRequest> months) {
        List<MonthResponse> monthsToResponse = new ArrayList<>();;
        try {
            for (MonthRequest month : months) {
                Optional<Month> foundMonth = repository.findByYearAndMonth(month.getYear(),month.getMonth());
                if (foundMonth.isPresent()) {
                    Month monthToSave = foundMonth.get();
                    monthToSave.setYear(month.getYear());
                    monthToSave.setMonth(month.getMonth());
                    monthToSave.setWorking_days(month.getWorking_days());
                    repository.save(monthToSave);
                } else {
                    repository.save(MonthRequest.toEntity(month));
                }
                monthsToResponse.add(MonthResponse.toResponse(MonthRequest.toEntity(month)));
            }
            return monthsToResponse;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public MonthResponse monthUpdate(MonthRequest update) {
        Month foundMonth = (repository.findByYearAndMonth(update.getYear(),update.getMonth())).orElseThrow(() -> new NotFoundException("Month not found."));
        try {
            updatedMonth = foundMonth;
            updatedMonth.setYear(update.getYear());
            updatedMonth.setMonth(update.getMonth());
            updatedMonth.setWorking_days(update.getWorking_days());
            return MonthResponse.toResponse(repository.save(updatedMonth));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MonthResponse> findAll(){
        try {
            return repository.findAll().stream()
                    .map(MonthResponse::toResponse)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
