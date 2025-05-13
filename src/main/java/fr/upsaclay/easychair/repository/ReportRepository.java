package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Report;
import fr.upsaclay.easychair.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByBodyIgnoreCase(String body);
    Optional<Report> findByEvaluationId(Long evaluationId);

}
