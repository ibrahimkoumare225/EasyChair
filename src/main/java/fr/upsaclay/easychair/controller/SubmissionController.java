/*
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Controller
@RequestMapping("/submissions")
public class SubmissionController {


    private final*/
/**//*
 SubmissionService submissionService;

    // GET /submissions
    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionService.findAll();
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
*/
