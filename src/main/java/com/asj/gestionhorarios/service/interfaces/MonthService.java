package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.request.MonthAndYearRequest;
import com.asj.gestionhorarios.model.request.MonthRequest;
import com.asj.gestionhorarios.model.response.Month.MonthResponse;

import java.util.List;

public interface MonthService {
    MonthResponse monthUpdate(MonthRequest update);

    MonthResponse save(MonthRequest month);

    MonthResponse findWorkingDay(MonthAndYearRequest monthAndYear);

    List<MonthResponse> updateOrSave(List<MonthRequest> months);

    List<MonthResponse> findAll();
}
