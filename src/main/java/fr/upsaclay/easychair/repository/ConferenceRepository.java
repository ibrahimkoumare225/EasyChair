package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

   /* @Query("SELECT c FROM Conference c LEFT JOIN FETCH c.organizers o LEFT JOIN FETCH o.user")
    List<Conference> findAllWithOrganizersAndUsers();*/

    List<Conference> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
