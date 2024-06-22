package com.vodafone.library.services;

import com.vodafone.library.model.Author;
import com.vodafone.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1980, 1, 1), "Nationality One",null);
        Author author2 = new Author(2L, "Author Two", LocalDate.of(1990, 2, 2), "Nationality Two",null);
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorRepository.findAll()).thenReturn(authors);

        ResponseEntity<List<Author>> response = authorService.getAllAuthors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authors, response.getBody());
    }

    @Test
    void testGetAuthorById_Found() {
        Author author = new Author(1L, "Author One", LocalDate.of(1980, 1, 1), "Nationality One",null);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        ResponseEntity<Author> response = authorService.getAuthorById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author, response.getBody());
    }

    @Test
    void testGetAuthorById_NotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Author> response = authorService.getAuthorById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        ResponseEntity<Author> response = authorService.createAuthor(author);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(author, response.getBody());
    }

    @Test
    void testUpdateAuthor_Found() {
        Author existingAuthor = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Author updatedAuthor = new Author(1L, "Author Updated", LocalDate.of(1980, 1, 1), "Nationality Updated", null);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        ResponseEntity<Author> response = authorService.updateAuthor(1L, updatedAuthor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAuthor, response.getBody());
    }

    @Test
    void testUpdateAuthor_NotFound() {
        Author updatedAuthor = new Author(1L, "Author Updated", LocalDate.of(1980, 1, 1), "Nationality Updated", null);

        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Author> response = authorService.updateAuthor(1L, updatedAuthor);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteAuthor_Found() {
        Author author = new Author(1L, "Author One", LocalDate.of(1980, 1, 1), "Nationality One",null );

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        ResponseEntity<?> response = authorService.deleteAuthor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAuthor_NotFound() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = authorService.deleteAuthor(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authorRepository, times(0)).deleteById(1L);
    }
}
