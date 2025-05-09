package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @EntityGraph(attributePaths = {"organizers"})
    Optional<Conference> findById(Long id);

    List<Conference> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrKeywordsContainingIgnoreCase(
            String title, String description, String keywords);

}