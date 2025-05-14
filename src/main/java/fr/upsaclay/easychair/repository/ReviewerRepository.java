package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Reviewer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long> {
    List<Reviewer> findByUserId(Long userId);
    List<Reviewer> findByConferenceId(Long conferenceId);

    List<Reviewer> findByUserEmail(String email);

    @Query("SELECT r FROM Reviewer r WHERE r.conference.id = :conferenceId AND r.user.email = :email")
    Optional<Reviewer> findByConferenceIdAndUserEmail(@Param("conferenceId") Long conferenceId, @Param("email") String email);

}