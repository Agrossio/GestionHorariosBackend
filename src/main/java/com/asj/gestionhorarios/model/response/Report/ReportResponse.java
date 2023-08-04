package com.asj.gestionhorarios.model.response.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    public Long worked_hours_project;
    public Long hours_estimate_project;
    public Long total_pending;
    public Long total_in_progress;
    public Long total_done;
    public Long total_cancelled;
    public Long total_reviewing;
}



