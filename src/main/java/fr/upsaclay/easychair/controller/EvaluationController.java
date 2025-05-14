
package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Reviewer;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.service.EvaluationService;
import fr.upsaclay.easychair.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/evaluations")
public class EvaluationController {


    private final EvaluationService evaluationService;
    private final UserService userService;

    //GET /evaluations
    @GetMapping
    public String showEvaluations(Model model, Authentication authentication) {
        List<Evaluation> evaluations = evaluationService.findAll();
        List<Evaluation> evaluationByReviewer = new ArrayList<>();
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElseThrow();
        for (Evaluation evaluation : evaluations) {
            if (evaluation.getSubmission().getReviewers().get(0).getId().equals(user.getId())) {
                evaluationByReviewer.add(evaluation);
            }
        }
        model.addAttribute("evaluations", evaluationByReviewer);
        return "dynamic/evaluation/listEvaluation";
    }

    //Get /evaluations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationByID(@PathVariable Long id) {
        Optional<Evaluation> evaluation = evaluationService.findOne(id);
    return evaluation.isPresent() ? ResponseEntity.ok(evaluation.get()) : ResponseEntity.notFound().build();
    }

    //POST /evaluations/
    @PostMapping
    public Evaluation createEvaluation(@RequestBody Evaluation evaluation) {
        return evaluationService.save(evaluation);}

    // PUT /evaluations/{id}
    @PutMapping
    public Evaluation updateEvaluation(@RequestBody Evaluation evaluation) {
        return evaluationService.update(evaluation);
    }

    // DELETE /evaluations/{id}
    @DeleteMapping("/{id}")
    public void deleteEvaluation(@PathVariable Long id) {
        evaluationService.delete(id);
    }
}

