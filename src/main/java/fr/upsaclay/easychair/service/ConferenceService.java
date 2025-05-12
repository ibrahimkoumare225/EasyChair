package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Conference;

import java.util.List;
import java.util.Optional;

public interface ConferenceService {

    Conference save(Conference conference);

    Conference update(Conference conference);

    Optional<Conference> findOne(Long id);

    List<Conference> findAll();

    void deleteById(Long id);

    List<Conference> findByTitleIgnoreCaseOrDescriptionIgnoreCase(String title, String description);

    /*List<Conference> searchByTerm(String searchTerm);*/

}
