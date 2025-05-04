package fr.upsaclay.easychair.controller;

import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // GET /authors
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.findAll();
    }

    // GET /authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorByID(@PathVariable Long id) {
        Optional<Author> author = authorService.findOne(id);
        return author.isPresent() ? (ResponseEntity<Author>) ResponseEntity.ok() : ResponseEntity.notFound().build();
    }

    // POST /authors
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorService.save(author);
    }

    // PUT /authors/{id}
    @PutMapping
    public Author updateAuthor(@RequestBody Author author) {
        return authorService.update(author);
    }

    // DELETE /authors/{id}
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
    }


}
