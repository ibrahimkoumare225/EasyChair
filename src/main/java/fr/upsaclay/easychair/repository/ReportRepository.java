package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {


    List<Report> findByBodyIgnoreCase(String body);
}
