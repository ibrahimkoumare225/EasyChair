package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Reviewer;

import java.util.List;
import java.util.Optional;

public interface ReviewerService {

    Reviewer save(Reviewer reviewer);

    Reviewer update(Reviewer reviewer);
    Optional<Reviewer> findOne(Long id);
    List<Reviewer> findAll();
    void delete(Long id);
}
