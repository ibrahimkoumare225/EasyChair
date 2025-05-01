package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
