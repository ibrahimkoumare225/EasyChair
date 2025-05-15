package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.*;
import fr.upsaclay.easychair.repository.ReviewerRepository;
import fr.upsaclay.easychair.service.EvaluationService;
import fr.upsaclay.easychair.service.ReviewerService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/evaluations")
public class EvaluationController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationController.class);
    private final ReviewerRepository reviewerRepository;
    private final EvaluationService evaluationService;
    private final UserService userService;

    public EvaluationController(ReviewerRepository reviewerRepository, EvaluationService evaluationService, UserService userService) {
        this.reviewerRepository = reviewerRepository;
        this.evaluationService = evaluationService;
        this.userService = userService;
    }

    @GetMapping
    public String showEvaluations(Model model, Authentication authentication) {
        String email = authentication.getName();
        logger.debug("Fetching evaluations for user with email: {}", email);

        var user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        logger.debug("Found user with ID: {}", user.getId());

        List<Evaluation> evaluations = evaluationService.findByReviewerUserId(user.getId());
        logger.debug("Found {} evaluations for user ID: {}", evaluations.size(), user.getId());

        var evaluationViews = evaluations.stream().map(evaluation -> {
            boolean hasReport = evaluation.getPosts().stream()
                    .filter(post -> post instanceof Report)
                    .anyMatch(report -> report.getReviewer().getUser().getId().equals(user.getId()));
            return new EvaluationView(evaluation, hasReport);
        }).collect(Collectors.toList());

        model.addAttribute("evaluationViews", evaluationViews);
        return "dynamic/evaluation/listEvaluation";
    }

    record EvaluationView(Evaluation evaluation, boolean hasReport) {}

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationByID(@PathVariable Long id) {
        logger.debug("Fetching evaluation with ID: {}", id);
        Optional<Evaluation> evaluation = evaluationService.findOne(id);
        return evaluation.isPresent() ? ResponseEntity.ok(evaluation.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Evaluation createEvaluation(@RequestBody Evaluation evaluation) {
        logger.debug("Creating new evaluation for submission ID: {}",
                evaluation.getSubmission() != null ? evaluation.getSubmission().getId() : "null");
        return evaluationService.save(evaluation);
    }

    @PutMapping
    public Evaluation updateEvaluation(@RequestBody Evaluation evaluation) {
        logger.debug("Updating evaluation with ID: {}", evaluation.getId());
        return evaluationService.update(evaluation);
    }

    @DeleteMapping("/{id}")
    public void deleteEvaluation(@PathVariable Long id) {
        logger.debug("Deleting evaluation with ID: {}", id);
        evaluationService.delete(id);
    }
}