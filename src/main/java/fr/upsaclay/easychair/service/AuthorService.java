package fr.upsaclay.easychair.service;

import fr.upsaclay.easychair.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author save(Author author);

    Author update(Author author);

    Optional<Author> findOne(long id);

    List<Author> findAll();

    void delete(long id);
}
