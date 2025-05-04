package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    //GET /evaluations
    @GetMapping
    public List<Evaluation> getAllEvaluations() { return evaluationService.findAll();}

    //Get /evaluations/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationByID(@PathVariable Long id) {
        Optional<Evaluation> evaluation = evaluationService.findOne(id);
    return evaluation.isPresent() ? ResponseEntity.ok(evaluation.get()) : ResponseEntity.notFound().build();
    }

    //POST /evaluations/
    @PostMapping
    public Evaluation createEvaluation(@RequestBody Evaluation evaluation) {return evaluationService.save(evaluation);}

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
