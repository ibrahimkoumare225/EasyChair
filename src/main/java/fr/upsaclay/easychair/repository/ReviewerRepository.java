package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
}
