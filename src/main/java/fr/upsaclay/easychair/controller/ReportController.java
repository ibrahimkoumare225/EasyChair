package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewerService reviewerService;

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

    @GetMapping("/showReportSub/{id_submission}")
    public String showReportSub(@PathVariable Long id_submission,
                                Model model) {
        List<Report> allReports = reportService.findAll();
        List<Report> reportSub = new ArrayList<>();
        List<User> users = new ArrayList<>();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User connectedUser = userService.findByEmail(email).orElseThrow();
        Long connectedUserId = connectedUser.getId();
        Long myReportId = null;

        for (Report report : allReports) {
            if (report.getEvaluation().getSubmission().getId().equals(id_submission)) {
                reportSub.add(report);
                users.add(report.getReviewer().getUser());

                // On identifie le rapport du reviewer connecté
                if (report.getReviewer().getUser().getId().equals(connectedUserId)) {
                    myReportId = report.getId();
                }
            }
        }

        if (reportSub.isEmpty()) {
            return "error/403";
        }

        model.addAttribute("reports", reportSub);
        model.addAttribute("reviewers", users); // inutile si tu passes les users via report
        model.addAttribute("myReportId", myReportId); // ID du rapport de l'utilisateur connecté
        return "dynamic/report/listReport";
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

    @PostMapping("/createReport")
    public String createReport(@ModelAttribute Report report,
                               @RequestParam Long id_evaluation,
                               @RequestParam("action") String action) {

        // Sinon, on continue pour créer un report
        Evaluation evaluation = evaluationService.findOne(id_evaluation)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        Submission submission = evaluation.getSubmission();
        if ("signal".equals(action)) {
            return "redirect:/alerts/alertForm/"+ submission.getId() ; // pas de save ici
        }
        report.setEvaluation(evaluation);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email).orElseThrow();
        List<Role> allRole = user.getRoles();
        Reviewer reviewer = null;
        for (Role role : allRole) {
            if (role.getRoleType() == RoleType.REVIEWER &&
                    role.getConference().getId().equals(evaluation.getSubmission().getConference().getId())) {
                reviewer = (Reviewer) role; // C’est ici que tu récupères le bon reviewer
                break;
            }
        }
        if (reviewer == null) {
            throw new RuntimeException("Reviewer not found for the current user and conference");
        }

        report.setReviewer(reviewer);

        report.setReviewer(reviewer);
        report.setDate(LocalDateTime.now());

        reportService.save(report);

        return "redirect:/conference";
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
