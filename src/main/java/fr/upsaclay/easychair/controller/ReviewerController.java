/*
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.service.ReviewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ReviewerController {

    private final ReviewerService reviewerService;

    // GET /reviewers
    @GetMapping
    public List<Reviewer> getAllReviewers() {
        return reviewerService.findAll();
    }

    // GET /reviewers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Reviewer> getReviewerById(@PathVariable Long id) {
        Optional<Reviewer> reviewer = reviewerService.findOne(id);
        return reviewer.isPresent() ? (ResponseEntity<Reviewer>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /reviewers
    @PostMapping
    public Reviewer createReviewer(@RequestBody Reviewer reviewer) {
        return reviewerService.save(reviewer);
    }

    // PUT /reviewers/{id}
    @PutMapping
    public Reviewer updateReviewer(@RequestBody Reviewer reviewer) {
        return reviewerService.update(reviewer);
    }

    // DELETE /reviewers/{id}
    @DeleteMapping("/{id}")
    public void deleteReviewer(@PathVariable Long id) {
        reviewerService.delete(id);
    }

}
*/
