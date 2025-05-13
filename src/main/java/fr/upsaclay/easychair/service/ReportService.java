package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    Report save(Report report);

    Report update(Report report);

    Optional<Report> findOne(Long id);
    List<Report> findAll();
    void delete(Long id);
    List<Report> findByBodyIgnoreCase(String body);

    Optional<Report> findByEvaluationId(Long evaluationId);
}
