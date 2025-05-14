package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    List<Organizer> findByUserId(Long userId);
    List<Organizer> findByConferenceId(Long conferenceId);

    @Query("SELECT o FROM Organizer o WHERE o.user.email = :email")
    List<Organizer> findByUserEmail(@Param("email") String email);
    void deleteByConferenceId(Long conferenceId);
}
