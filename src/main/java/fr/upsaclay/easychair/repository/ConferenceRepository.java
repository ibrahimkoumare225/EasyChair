package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
