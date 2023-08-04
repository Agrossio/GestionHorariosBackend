package com.asj.gestionhorarios.service.impl;

import com.asj.gestionhorarios.exception.customExceptions.NotFoundException;
import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Status;
import com.asj.gestionhorarios.model.response.Report.ReportResponse;
import com.asj.gestionhorarios.repository.ProjectRepository;
import com.asj.gestionhorarios.repository.ReportRepository;
import com.asj.gestionhorarios.service.interfaces.ReportService;
import com.asj.gestionhorarios.utils.StatusFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final ProjectRepository projectRepository;
    @Override
    public ReportResponse findByProjectAndEndDate(Long project_id, LocalDate end_date, boolean isDisabled) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        List<String> statusList = Arrays.asList("PENDING", "IN_PROGRESS", "DONE", "CANCELLED", "REVIEWING");
        Map<String, Long> statusCountMap = new HashMap<>();
        statusList.forEach((status) -> {
            long count = repository.countByProjectAndEndDateAndStatus(project, end_date.getMonthValue(), end_date.getYear(), StatusFactory.newStatus(status), isDisabled);
            statusCountMap.put(status, count);
        });

        try {
            ReportResponse response = new ReportResponse();
            Long worked_hours_project = repository.sumWorkedHoursByProjectAndEndDate(project, end_date.getMonthValue(), end_date.getYear(), isDisabled);
            response.setWorked_hours_project(worked_hours_project != null ? worked_hours_project : 0);
            Long hours_estimate_project = repository.sumHoursEstimateByProjectAndEndDate(project, end_date.getMonthValue(), end_date.getYear(), isDisabled);
            response.setHours_estimate_project(hours_estimate_project != null ? hours_estimate_project : 0);

            response.setTotal_pending(statusCountMap.get("PENDING"));
            response.setTotal_in_progress(statusCountMap.get("IN_PROGRESS"));
            response.setTotal_done(statusCountMap.get("DONE"));
            response.setTotal_cancelled(statusCountMap.get("CANCELLED"));
            response.setTotal_reviewing(statusCountMap.get("REVIEWING"));

            return response;
        } catch (PersistenceException e){
            throw new PersistenceException(e.getMessage());
        }
    }
}
