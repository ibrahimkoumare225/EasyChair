package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByTitleIgnoreCase(String title);



}
