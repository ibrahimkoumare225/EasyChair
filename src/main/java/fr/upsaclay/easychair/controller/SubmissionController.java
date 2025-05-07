
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.SubmissionService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final UserService userService;

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

    @GetMapping("/ajouterSubmission")
    public String showAddSubForm(Model model) {
        model.addAttribute("submission", new Submission());
        return "dynamic/submission/createSubmission";
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
    public Submission createSubmission(@RequestBody Submission submission) {
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
