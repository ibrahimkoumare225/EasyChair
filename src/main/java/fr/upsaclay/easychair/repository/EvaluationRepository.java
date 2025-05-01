package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
