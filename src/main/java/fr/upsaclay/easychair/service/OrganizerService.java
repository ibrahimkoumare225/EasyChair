package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Organizer;

import java.util.List;
import java.util.Optional;

public interface OrganizerService {

    Organizer save(Organizer organizer);

    Organizer update(Organizer organizer);

    Optional<Organizer> findOne(Long id);

    List<Organizer> findAll();

    void deleteById(Long id);
}
