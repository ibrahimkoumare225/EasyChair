package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;

import java.util.List;
import java.util.Optional;

public interface SubmissionService {

    Submission save(Submission submission);


    Submission  update(Submission  submission);


    Optional<Submission> findOne(Long id);

    List<Submission> findAll();

    void delete(Long id);

    List<Submission> findByTitleIgnoreCase(String title);

    List<Submission> findSubmissionsByAuthor(User user);

    List <Submission> findSubmissionsByConference(Conference conference);
}
