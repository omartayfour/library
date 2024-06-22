package com.vodafone.library.controller;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.services.BookService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() throws Exception {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book1 = new Book(1L, "Book One", author, "1234567890", LocalDate.of(2020, 1, 1), "Genre1", true);
        Book book2 = new Book(2L, "Book Two", author, "0987654321", LocalDate.of(2021, 2, 2), "Genre2", true);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(new ResponseEntity<>(books, HttpStatus.OK));

        ResponseEntity<List<Book>> result = bookService.getAllBooks();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(books, result.getBody());
    }

    @Test
    void testGetBookById() throws Exception {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book One", author, "1234567890", LocalDate.of(2020, 1, 1), "Genre1", true);

        when(bookService.getBookById(anyLong())).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));

        ResponseEntity<Book> result = bookService.getBookById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(book, result.getBody());
    }

    @Test
    void testCreateBook() {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book One", author, "1234567890", LocalDate.of(2020, 1, 1), "Genre1", true);

        when(bookService.createBook(any(Book.class))).thenReturn(new ResponseEntity<>(book, HttpStatus.CREATED));

        ResponseEntity<Book> response = bookController.createBook(book);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testUpdateBook() {
        Author author = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book One", author, "1234567890", LocalDate.of(2020, 1, 1), "Genre1", true);

        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(new ResponseEntity<>(book, HttpStatus.OK));

        ResponseEntity<Book> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void testDeleteBook() {
        when(bookService.deleteBook(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
