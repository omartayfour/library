package com.vodafone.library.controller;

import com.vodafone.library.model.Author;
import com.vodafone.library.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAuthors() throws Exception {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Author author2 = new Author(2L, "Author Two", LocalDate.of(1992, 2, 2), "Nationality2", null);
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorService.getAllAuthors()).thenReturn(new ResponseEntity<>(authors, HttpStatus.OK));

        ResponseEntity<List<Author>> result = authorService.getAllAuthors();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(authors, result.getBody());
    }

    @Test
    void testGetAuthorById() throws Exception {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);

        when(authorService.getAuthorById(anyLong())).thenReturn(new ResponseEntity<>(author, HttpStatus.OK));

        ResponseEntity<Author> result = authorService.getAuthorById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(author, result.getBody());
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);

        when(authorService.createAuthor(any(Author.class))).thenReturn(new ResponseEntity<>(author, HttpStatus.CREATED));

        ResponseEntity<Author> response = authorController.createAuthor(author);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(author, response.getBody());
    }

    @Test
    void testUpdateAuthor() {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);

        when(authorService.updateAuthor(anyLong(), any(Author.class))).thenReturn(new ResponseEntity<>(author, HttpStatus.OK));

        ResponseEntity<Author> response = authorController.updateAuthor(1L, author);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(author, response.getBody());
    }

    @Test
    void testDeleteAuthor() {
        when(authorService.deleteAuthor(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = authorController.deleteAuthor(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
