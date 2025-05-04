package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description);
}
