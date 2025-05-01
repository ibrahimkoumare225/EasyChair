package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
