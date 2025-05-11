package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Conference;

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

    List<Conference> findConferencesByUserEmail(String email);
}