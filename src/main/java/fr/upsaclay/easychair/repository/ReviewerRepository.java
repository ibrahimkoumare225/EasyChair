package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    List<Reviewer> findByUserId(Long userId);
    List<Reviewer> findByConferenceId(Long conferenceId);
}