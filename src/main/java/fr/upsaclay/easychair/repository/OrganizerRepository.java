package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    List<Organizer> findByUserId(Long userId);
    List<Organizer> findByConferenceId(Long conferenceId);
}
