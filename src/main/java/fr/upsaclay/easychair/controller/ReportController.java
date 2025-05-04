package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Report;
import fr.upsaclay.easychair.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // GET /reports
    @GetMapping
    public List<Report> getAllReports() {
        return reportService.findAll();
    }

    // GET /reports/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Optional<Report> report = reportService.findOne(id);
        return report.isPresent() ? (ResponseEntity<Report>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /reports
    @PostMapping
    public Report createReport(@RequestBody Report report) {
        return reportService.save(report);
    }

    // PUT /reports/{id}
    @PutMapping
    public Report updateReport(@RequestBody Report report) {
        return reportService.update(report);
    }

    // DELETE /reports/{id}
    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable Long id) {
        reportService.delete(id);
    }
}
