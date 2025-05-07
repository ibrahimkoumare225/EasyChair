package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByUser(User user);
}
