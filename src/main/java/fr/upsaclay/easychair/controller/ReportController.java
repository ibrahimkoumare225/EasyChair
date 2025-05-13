package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Report;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.service.EvaluationService;
import fr.upsaclay.easychair.service.ReportService;
import fr.upsaclay.easychair.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EvaluationService  evaluationService;

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

    @PostMapping("/ajouterReport")
    public String showAddReportForm(@RequestParam Long evaluationId, Model model) {
        Optional<Evaluation> evaluation = evaluationService.findOne(evaluationId);

        if (evaluation.isEmpty()) {
            return "error/404";
        }

        Optional<Report> report = reportService.findByEvaluationId(evaluationId);

        if (!report.isPresent()) {
            model.addAttribute("evaluation", evaluation.get());
            model.addAttribute("report", new Report());
            return "dynamic/evaluation/reportForm";
        } else {
            return "redirect:/posts/form/ajouterPost/" + evaluationId;
        }
    }

    @PostMapping("/modifierReport")
    public String showUpdateReportForm(@RequestParam Long reportId, Model model) {
        Optional<Report> report = reportService.findOne(reportId);
        if (report.isPresent()) {
            model.addAttribute("submission",report.get().getEvaluation());
            model.addAttribute("report", report.get());
            return "dynamic/evaluation/reportForm";
        }
        else return "error/404";
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
