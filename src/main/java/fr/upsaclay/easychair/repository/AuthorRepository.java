package fr.upsaclay.easychair.repository;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByUserId(Long userId);
    List<Author> findByConferenceId(Long conferenceId);
    List<Author> findByUser(User user);
    void deleteByConferenceId(Long conferenceId);
    @Query("SELECT a FROM Author a WHERE a.user.email = :email")
    List<Author> findByUserEmail(@Param("email") String email);

    @Query("SELECT a FROM Author a WHERE a.conference.id = :conferenceId AND a.user.email = :email")
    Optional<Author> findByConferenceIdAndUserEmail(@Param("conferenceId") Long conferenceId, @Param("email") String email);

}
