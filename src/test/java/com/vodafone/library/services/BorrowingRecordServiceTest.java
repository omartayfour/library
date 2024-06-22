package com.vodafone.library.services;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.model.BorrowingRecord;
import com.vodafone.library.model.Customer;
import com.vodafone.library.repository.BookRepository;
import com.vodafone.library.repository.BorrowingRecordRepository;
import com.vodafone.library.repository.CustomerRepository;
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

public class BorrowingRecordServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllBorrowingRecords() {
        List<BorrowingRecord> mockRecords = new ArrayList<>();
        mockRecords.add(new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14)));
        mockRecords.add(new BorrowingRecord(2L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14)));
        when(borrowingRecordRepository.findAll()).thenReturn(mockRecords);

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordService.getAllBorrowingRecords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetBorrowingRecordById_existingId() {
        BorrowingRecord mockRecord = new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14));
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(mockRecord));

        ResponseEntity<BorrowingRecord> response = borrowingRecordService.getBorrowingRecordById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRecord, response.getBody());
    }

    @Test
    void testGetBorrowingRecordById_nonExistingId() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<BorrowingRecord> response = borrowingRecordService.getBorrowingRecordById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateBorrowingRecord() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        BorrowingRecord newRecord = new BorrowingRecord(null, customer, book, LocalDate.now(), LocalDate.now().plusDays(14));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(newRecord);

        ResponseEntity<BorrowingRecord> response = borrowingRecordService.createBorrowingRecord(newRecord);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newRecord.getCustomer(), response.getBody().getCustomer());
        assertEquals(newRecord.getBook(), response.getBody().getBook());
    }

    @Test
    void testCreateBorrowingRecord_bookNotAvailable() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        BorrowingRecord newRecord = new BorrowingRecord(null, customer, book, LocalDate.now(), LocalDate.now().plusDays(14));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<BorrowingRecord> response = borrowingRecordService.createBorrowingRecord(newRecord);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testCreateBorrowingRecord_customerNotFound() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        BorrowingRecord newRecord = new BorrowingRecord(null, new Customer(), book, LocalDate.now(), LocalDate.now().plusDays(14));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(RuntimeException.class, () -> borrowingRecordService.createBorrowingRecord(newRecord));
    }

    @Test
    void testCreateBorrowingRecord_bookNotFound() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        BorrowingRecord newRecord = new BorrowingRecord(null, customer, new Book(), LocalDate.now(), LocalDate.now().plusDays(14));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(RuntimeException.class, () -> borrowingRecordService.createBorrowingRecord(newRecord));
    }

    @Test
    void testUpdateBorrowingRecord_existingId() {
        BorrowingRecord existingRecord = new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14));
        BorrowingRecord updatedRecordDetails = new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14));

        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(existingRecord));
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(new Customer()));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(new Book()));

        ResponseEntity<?> response = borrowingRecordService.updateBorrowingRecord(1L, updatedRecordDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateBorrowingRecord_nonExistingId() {
        BorrowingRecord updatedRecordDetails = new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14));
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = borrowingRecordService.updateBorrowingRecord(1L, updatedRecordDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateBorrowingRecord_bookAvailable() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        BorrowingRecord existingRecord = new BorrowingRecord(1L, new Customer(), book, LocalDate.now(), LocalDate.now().plusDays(14));
        BorrowingRecord updatedRecordDetails = new BorrowingRecord(1L, new Customer(), book, LocalDate.now(), LocalDate.now().plusDays(14));

        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(existingRecord));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

        ResponseEntity<?> response = borrowingRecordService.updateBorrowingRecord(1L, updatedRecordDetails);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book is not available for borrowing", response.getBody());
    }

    @Test
    void testDeleteBorrowingRecord_existingId() {
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book 1",author1,  "978-3-16-148410-0", LocalDate.of(2020, 1, 1), "Fiction", true);
        BorrowingRecord existingRecord = new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14));
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(existingRecord));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

        ResponseEntity<Void> response = borrowingRecordService.deleteBorrowingRecord(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(borrowingRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBorrowingRecord_nonExistingId() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = borrowingRecordService.deleteBorrowingRecord(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBorrowingRecordsByCustomerId_existingId() {
        List<BorrowingRecord> mockRecords = new ArrayList<>();
        mockRecords.add(new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14)));
        when(borrowingRecordRepository.findByCustomerId(1L)).thenReturn(mockRecords);

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordService.getBorrowingRecordsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetBorrowingRecordsByCustomerId_nonExistingId() {
        when(borrowingRecordRepository.findByCustomerId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordService.getBorrowingRecordsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetBorrowingRecordsByBookId_existingId() {
        List<BorrowingRecord> mockRecords = new ArrayList<>();
        mockRecords.add(new BorrowingRecord(1L, new Customer(), new Book(), LocalDate.now(), LocalDate.now().plusDays(14)));
        when(borrowingRecordRepository.findByBookId(1L)).thenReturn(mockRecords);

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordService.getBorrowingRecordsByBookId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetBorrowingRecordsByBookId_nonExistingId() {
        when(borrowingRecordRepository.findByBookId(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordService.getBorrowingRecordsByBookId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }
}
