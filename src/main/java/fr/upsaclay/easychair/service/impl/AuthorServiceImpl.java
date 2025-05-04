package fr.upsaclay.easychair.service.impl;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.repository.AuthorRepository;
import fr.upsaclay.easychair.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author update(Author author) {
        return findOne(author.getId()).map(existingAuthor ->{
               return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new EntityNotFoundException("Author introuvable avec l’ID : " + author.getId()));
    }

    @Override
    public Optional<Author> findOne(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public void delete(long id) {
        authorRepository.deleteById(id);
    }
}
