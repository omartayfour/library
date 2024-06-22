package com.vodafone.library.services;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllBooks() {
        List<Book> mockBooks = new ArrayList<>();
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        mockBooks.add(new Book(1L, "Book 1", author1,"978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true));
        mockBooks.add(new Book(2L, "Book 2", author1, "978-3-16-148410-1", LocalDate.of(2021, 2, 2), "Fantasy", true));
        when(bookRepository.findAll()).thenReturn(mockBooks);

        ResponseEntity<List<Book>> response = bookService.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetBookById_existingId() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book newBook = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(newBook));

        ResponseEntity<Book> response = bookService.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newBook, response.getBody());
    }

    @Test
    void testGetBookById_nonExistingId() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookService.getBookById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateBook() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book newBook = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        ResponseEntity<Book> response = bookService.createBook(newBook);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newBook.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testUpdateBook_existingId() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book existingBook = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        Book updatedBookDetails = new Book(1L, "Updated Book", author1 ,"978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBookDetails);

        ResponseEntity<Book> response = bookService.updateBook(1L, updatedBookDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBookDetails.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testUpdateBook_nonExistingId() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book updatedBookDetails = new Book(1L, "Updated Book", author1 ,"978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookService.updateBook(1L, updatedBookDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBook_existingId_availableBook() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book existingBook = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        ResponseEntity<?> response = bookService.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteBook_nonExistingId() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.deleteBook(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteBook_borrowedBook() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book borrowedBook = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(borrowedBook));

        ResponseEntity<?> response = bookService.deleteBook(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSearchByTitle_existingTitle() {
        // Mock data
        List<Book> mockBooks = new ArrayList<>();
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        mockBooks.add(new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true));
        mockBooks.add(new Book(1L, "Book Title 2", author1, "978-3-16-148410-1", LocalDate.of(2000, 1, 1), "Genre 2", true));
        when(bookRepository.findByTitleContaining("Book")).thenReturn(mockBooks);

        ResponseEntity<List<Book>> response = bookService.searchByTitle("Book");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testSearchByTitle_nonExistingTitle() {
        when(bookRepository.findByTitleContaining("Nonexistent")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Book>> response = bookService.searchByTitle("Nonexistent");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testSearchByAuthorId_existingId() {
        // Mock data
        List<Book> mockBooks = new ArrayList<>();
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        mockBooks.add(new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true));
        when(bookRepository.findByAuthorId(1L)).thenReturn(mockBooks);

        ResponseEntity<List<Book>> response = bookService.searchByAuthorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchByAuthorId_nonExistingId() {
        when(bookRepository.findByAuthorId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<Book>> response = bookService.searchByAuthorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testSearchByIsbn_existingIsbn() {
        // Mock data
        List<Book> mockBooks = new ArrayList<>();
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        mockBooks.add(new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true));
        when(bookRepository.findByIsbn("978-3-16-148410-0")).thenReturn(mockBooks);

        ResponseEntity<List<Book>> response = bookService.searchByIsbn("978-3-16-148410-0");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testSearchByIsbn_nonExistingIsbn() {
        when(bookRepository.findByIsbn("978-3-16-148410-0")).thenReturn(new ArrayList<>());

        ResponseEntity<List<Book>> response = bookService.searchByIsbn("978-3-16-148410-0");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
