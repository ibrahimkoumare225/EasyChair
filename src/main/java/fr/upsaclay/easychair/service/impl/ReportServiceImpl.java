package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Report;
import fr.upsaclay.easychair.repository.ReportRepository;
import fr.upsaclay.easychair.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report update(Report report) {
        return findOne(report.getId()).map(existingReport ->{
            existingReport.setBody(report.getBody());
            existingReport.setSpecDegree(report.getSpecDegree());
            return existingReport;
        }).orElseThrow(() -> new EntityNotFoundException("Report introuvable avec l’ID : " + report.getId()));
    }

    @Override
    public Optional<Report> findOne(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    public List<Report> findByBodyIgnoreCase(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Body peut pas être null");
        }
        return reportRepository.findByBodyIgnoreCase(body);
    }
    @Override
    public Optional<Report> findByEvaluationId(Long evaluationId) {
        return reportRepository.findByEvaluationId(evaluationId);
    }

}
