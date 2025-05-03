package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
