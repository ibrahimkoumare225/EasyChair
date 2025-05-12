package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @EntityGraph(attributePaths = {"organizers"})
    Optional<Conference> findById(Long id);

    @EntityGraph(attributePaths = {"organizers"})
    @Query("SELECT c FROM Conference c")
    List<Conference> findAllWithOrganizers();

    List<Conference> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrKeywordsContainingIgnoreCase(
            String title, String description, String keywords);

    @Query("SELECT DISTINCT c FROM Conference c " +
            "LEFT JOIN c.organizers o " +
            "LEFT JOIN c.reviewers r " +
            "LEFT JOIN c.authors a " +
            "WHERE o.user.email = :email OR r.user.email = :email OR a.user.email = :email")
    List<Conference> findConferencesByUserEmail(@Param("email") String email);
}