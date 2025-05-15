package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.Reviewer;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<Author> findByConferenceID(Long conferenceId);

    Author save(Author author);

    Author update(Author author);

    Optional<Author> findOne(Long id);

    List<Author> findAll();

    void delete(Long id);
}
