package fr.upsaclay.easychair.service.impl;

import static org.junit.jupiter.api.Assertions.*;


import fr.upsaclay.easychair.model.Author;
import fr.upsaclay.easychair.model.Submission;
import fr.upsaclay.easychair.model.User;
import fr.upsaclay.easychair.model.enumates.RoleType;
import fr.upsaclay.easychair.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;
    private Author author;
    private Author author2;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test


        author = new Author();
        author.setId(1L);
        author.setRole(RoleType.AUTHOR);


        author2 = new Author();
        author2.setId(2L);
        author2.setRole(RoleType.AUTHOR);

        authorList = Arrays.asList(author, author2);
    }

    @Test
    void save_ShouldReturnSavedAuthor() {
        Author newAuthor = new Author();
        newAuthor.setRole(RoleType.AUTHOR);
        // Given

        when(authorRepository.save(any(Author.class))).thenAnswer(invocation ->{
            Author author = invocation.getArgument(0);
            author.setId(1L);
            return author;
        });

        // When
        Author savedAuthor = authorService.save(author);

        // Then
        assertNotNull(savedAuthor);
        assertSame(author,savedAuthor);
        assertEquals(1L,savedAuthor.getId());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void update_WhenAuthorExists_ShouldReturnUpdatedAuthor() {
        // Given
        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setRole(RoleType.AUTHOR);


        Author newAuthorData = new Author();
        newAuthorData.setId(1L);
        newAuthorData.setRole(RoleType.AUTHOR);
        newAuthorData.getSubmissions().add(new Submission());

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(newAuthorData);

        // When
        Author updatedAuthor = authorService.update(newAuthorData);

        // Then
        assertNotNull(updatedAuthor);
        assertEquals(1L, updatedAuthor.getId());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void update_WhenAuthorDoesNotExist_ShouldThrowEntityNotFoundException() {
        // Given
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> authorService.update(author));
        verify(authorRepository, times(1)).findById(author.getId());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void findOne_WhenAuthorExists_ShouldReturnAuthor() {
        // Given
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        // When
        Optional<Author> foundAuthor = authorService.findOne(1L);

        // Then
        assertTrue(foundAuthor.isPresent());
        assertEquals(author.getId(), foundAuthor.get().getId());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void findOne_WhenAuthorDoesNotExist_ShouldReturnEmptyOptional() {
        // Given
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Optional<Author> foundAuthor = authorService.findOne(1L);

        // Then
        assertFalse(foundAuthor.isPresent());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ShouldReturnAllAuthors() {
        // Given
        when(authorRepository.findAll()).thenReturn(authorList);

        // When
        List<Author> authors = authorService.findAll();

        // Then
        assertNotNull(authors);
        assertEquals(2, authors.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void delete_ShouldDeleteAuthor() {
        // Given
        doNothing().when(authorRepository).deleteById(anyLong());

        // When
        authorService.delete(1L);

        // Then
        verify(authorRepository, times(1)).deleteById(1L);
    }
}