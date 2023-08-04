package com.asj.gestionhorarios.repository;

import com.asj.gestionhorarios.model.entity.Project;
import com.asj.gestionhorarios.model.entity.Status;
import com.asj.gestionhorarios.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Task, Long> {
    @Query("SELECT SUM(t.worked_hours) FROM Task t WHERE t.project = :project AND MONTH(t.end_date) = :month AND YEAR(t.end_date) = :year AND t.disabled = :disabled")
    Long sumWorkedHoursByProjectAndEndDate (@Param("project") Project project, @Param("month") Integer month, @Param("year") Integer year, boolean disabled);
    @Query("SELECT SUM(t.hours_estimate) FROM Task t WHERE t.project = :project AND MONTH(t.end_date) = :month AND YEAR(t.end_date) = :year AND t.disabled = :disabled")
    Long sumHoursEstimateByProjectAndEndDate (@Param("project") Project project, @Param("month") Integer month, @Param("year") Integer year, boolean disabled);
    @Query("SELECT COUNT (t) FROM Task t WHERE t.project = :project AND MONTH(t.end_date) = :month AND YEAR(t.end_date) = :year AND t.status = :status AND t.disabled = :disabled")
    Long countByProjectAndEndDateAndStatus(@Param("project") Project project, @Param("month") Integer month, @Param("year") Integer year, @Param("status") Status status, @Param("disabled") boolean disabled);
}