package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.model.Alert;
import fr.upsaclay.easychair.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private ReviewerService reviewerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ConferenceService conferenceService;


    // GET /alerts/alertForm
    @GetMapping("/alertForm/{id_submission}")
    public String showAlertForm(@PathVariable Long id_submission, Model model) {
        model.addAttribute("id_submission", id_submission);
        model.addAttribute("alert", new Alert());
        return "dynamic/alert/alertForm";
    }

    @PostMapping("/saveAlert/{id_submission}")
    public String saveAlert(@ModelAttribute Alert alert, @PathVariable Long id_submission, Model model) {

        Submission submission = submissionService.findOne(id_submission).orElseThrow();
        alert.setSubmission(submission);
        if (alertService.findAlertBySubmission(submission))
        {
            return "redirect:/alerts/alertForm/" + id_submission;
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByEmail(email).orElseThrow();
        Reviewer reviewer = reviewerService.findByUserId(user.getId()).orElseThrow();
        alert.setReviewer(reviewer);
        Conference conference = submission.getConference();
        Organizer organizer = conference.getOrganizers().get(0);
        alert.setOrganizer(organizer);
        alertService.save(alert);
        return "redirect:/alerts";
    }

    // GET /alerts
    @GetMapping
    public String showAllAlerts(Model model) {
        List<Alert> alerts = alertService.findAll();
        if (alerts.isEmpty()) {
            return "error/403";
        }
        model.addAttribute("alerts", alerts);
        return "dynamic/alert/listAlert";
    }

    //GET /alerts/conference/{id_conference}
    @GetMapping("/conference/{id_conference}")
    public String showAllAlertsForConference(@PathVariable Long id_conference, Model model) {
        List<Alert> allAlerts = alertService.findAll();
        List<Alert> alertConf = new ArrayList<>();
        for (Alert alert : allAlerts) {
            if (alert.getSubmission() != null &&
                    alert.getSubmission().getConference() != null &&
                    alert.getSubmission().getConference().getId().equals(id_conference)) {
                alertConf.add(alert);
            }
        }
        model.addAttribute("alerts", alertConf);
        return "dynamic/alert/listAlert";
    }
    // GET /alerts/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Long id) {
        Optional<Alert> Alert = alertService.findOne(id);
        return Alert.isPresent() ? (ResponseEntity<Alert>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // PUT /alerts/{id}
    @PutMapping
    public Alert updateAlert(@RequestBody Alert alert) {
        return alertService.update(alert);
    }

    // DELETE /alerts/{id}
    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.delete(id);
    }

}
