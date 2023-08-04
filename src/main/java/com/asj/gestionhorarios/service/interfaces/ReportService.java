package com.asj.gestionhorarios.service.interfaces;

import com.asj.gestionhorarios.model.response.Report.ReportResponse;

import java.time.LocalDate;

public interface ReportService {
    ReportResponse findByProjectAndEndDate(Long project_id, LocalDate end_date, boolean isDisabled);
}
