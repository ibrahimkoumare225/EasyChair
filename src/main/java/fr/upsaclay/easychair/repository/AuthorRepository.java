package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByUserId(Long userId);
    List<Author> findByConferenceId(Long conferenceId);
    List<Author> findByUser(User user);

}
