
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.Phase;
import fr.upsaclay.easychair.model.enumates.SubType;
import fr.upsaclay.easychair.service.ConferenceService;
import fr.upsaclay.easychair.service.SubmissionService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserService userService;
    private final ConferenceService conferenceService;

    @GetMapping("/user/{userId}")
    public String showUserSubmissions(@PathVariable Long userId, Model model) {
        Optional<User> user = userService.findOne(userId);
        if (user.isPresent()) {
            List<Submission> submissions = submissionService.findSubmissionsByAuthor(user.get());
            model.addAttribute("submissions", submissions);
            model.addAttribute("user", user.get());
            return "/dynamic/submission/listSubmission"; // Nom de la vue Thymeleaf
        } else {
            return "error/404";
        }
    }

    @GetMapping("/submissionDetail/{id}")
    public String showDetailSubmission(Model model, @PathVariable Long id) {
        Optional<Submission> submission = submissionService.findOne(id);
        if (submission.isPresent()) {
            model.addAttribute("submission", submission.get());
            return  "dynamic/submission/detailSubmission";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/ajouterSubmission")
    public String showAddSubForm(@RequestParam Long conferenceId, Model model) {
        //Ajout de la conference pour rattachement de la submission

        Optional<Conference> conference = conferenceService.findOne(conferenceId);
        //@Todo condition d'accès a rajouter avec verif du user par rapport a la conf
        if (conference.isPresent()){
            model.addAttribute("conference", conference.get());
            model.addAttribute("submission", new Submission());
            return "dynamic/submission/submissionForm";
        }
        else{
            return "error/404";
        }

    }
    @PostMapping("/modifierSubmission")
    public String showModifySubForm(@RequestParam Long submissionId,  Model model) {
        //@Todo condition d'accès a rajouter avec verif du user par rapport a la conf
        Optional<Submission> submission= submissionService.findOne(submissionId);
        if (submission.isPresent()) {
            model.addAttribute("conference", submission.get().getConference());
            model.addAttribute("submission", submission.get());
            return "dynamic/submission/submissionForm";
        }
        else{
            return "error/404";
        }
    }

    public String saveSubmission(Submission submission) {
        submissionService.save(submission);
        return "redirect:/conference";
    }


    // GET /submissions/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        Optional<Submission> submission = submissionService.findOne(id);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /submissions
    @PostMapping
    public Submission createSubmission(@RequestBody Submission submission,Model model) {
        //@Todo avis si c'est propre ?
        submission.setConference((Conference)model.getAttribute("conference"));
        submission.setCreationDate(Date.from(Instant.now()));
        submission.setStatus(SubType.PROGRESS);
        submission.setEvaluation(new Evaluation());
        //relation bi directionnelle
        submission.getEvaluation().setSubmission(submission);
        return submissionService.save(submission);
    }


    // PUT /submissions/{id}
    @PutMapping
    public Submission updateSubmission(@RequestBody Submission submission) {
        return submissionService.update(submission);
    }

    // DELETE /submissions/{id}
    @DeleteMapping("/{id}")
    public void deleteSubmission(@PathVariable Long id) {
        submissionService.delete(id);
    }

    // GET /submissions/search
    @GetMapping("/search")
    public List<Submission> searchByTitle(@RequestBody String title) {
        return submissionService.findByTitleIgnoreCase(title);
    }
}
