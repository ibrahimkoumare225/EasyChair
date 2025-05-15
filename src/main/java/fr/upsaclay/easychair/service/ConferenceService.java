package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Conference;
import fr.upsaclay.easychair.model.enumates.Phase;

import java.util.List;
import java.util.Optional;

public interface ConferenceService {


    public List<Conference> findAllWithOrganizers();

    List<Conference> findAll();

    List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description, String query);

    Optional<Conference> findOne(Long id);

    Conference save(Conference conference);

    Conference update(Conference conference);

    void deleteById(Long id);

    List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description);

    /*List<Conference> searchByTerm(String searchTerm);*/

    List<Conference> findConferencesByUserEmail(String email);

    List<Conference> findConferencesByOrganizerEmail(String email);

    List<Conference> findConferencesByAuthorEmail(String email);

    List<Conference> findConferencesByReviewerEmail(String email);


}
