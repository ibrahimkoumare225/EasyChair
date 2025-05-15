package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    @Query("SELECT DISTINCT e FROM Evaluation e " +
            "JOIN e.submission s " +
            "JOIN s.reviewers r " +
            "JOIN r.user u " +
            "WHERE u.id = :userId")
    List<Evaluation> findBySubmissionReviewersUserId(@Param("userId") Long userId);
}