package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description);

    List<Conference> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrKeywordsContainingIgnoreCase(
            String title, String description, String keyword
    );
    @Query( "SELECT DISTINCT c FROM Conference c " +
            "LEFT JOIN FETCH c.keywords k " +
            //"WHERE c.hiddenConf =false AND("+
            "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(k) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Conference> searchByTermInTitleOrDescriptionOrKeywords(@Param("searchTerm") String searchTerm);

}
