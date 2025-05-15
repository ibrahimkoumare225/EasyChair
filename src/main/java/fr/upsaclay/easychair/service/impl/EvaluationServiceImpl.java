package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Evaluation;
import fr.upsaclay.easychair.repository.EvaluationRepository;
import fr.upsaclay.easychair.service.EvaluationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;

    @Override
    public Evaluation save(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public Evaluation update(Evaluation evaluation) {
        return findOne(evaluation.getId()).map(existingEval->{
                return evaluationRepository.save(existingEval);
        }).orElseThrow(() -> new EntityNotFoundException("Evaluation introuvable avec l’ID : " + evaluation.getId()));
    }

    @Override
    public Optional<Evaluation> findOne(Long id) {
        return evaluationRepository.findById(id);
    }

    @Override
    public List<Evaluation> findAll() {
        return evaluationRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        evaluationRepository.deleteById(id);
    }

    @Override
    public List<Evaluation> findByReviewerUserId(Long userId) {
        return evaluationRepository.findBySubmissionReviewersUserId(userId);
    }
}
