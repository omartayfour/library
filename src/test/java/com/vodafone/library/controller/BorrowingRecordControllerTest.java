package com.vodafone.library.controller;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.model.BorrowingRecord;
import com.vodafone.library.model.Customer;
import com.vodafone.library.services.BorrowingRecordService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BorrowingRecordControllerTest {

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBorrowingRecords() throws Exception {
        Customer customer1 = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book1 = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record1 = new BorrowingRecord(1L, customer1, book1, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));

        Customer customer2 = new Customer(2L, "Omar Mohamed", "omarmohamed@example.com", "01198765432", "password2", "Cairo", null);
        Author author2 = new Author(2L, "Author Two", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book2 = new Book(2L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record2 = new BorrowingRecord(2L, customer2, book2, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));

        List<BorrowingRecord> records = Arrays.asList(record1, record2);

        when(borrowingRecordService.getAllBorrowingRecords()).thenReturn(new ResponseEntity<>(records, HttpStatus.OK));

        ResponseEntity<List<BorrowingRecord>> result = borrowingRecordService.getAllBorrowingRecords();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(records, result.getBody());
    }

    @Test
    void testGetBorrowingRecordById() throws Exception {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record = new BorrowingRecord(1L, customer, book, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));

        when(borrowingRecordService.getBorrowingRecordById(anyLong())).thenReturn(new ResponseEntity<>(record, HttpStatus.OK));

        ResponseEntity<BorrowingRecord> result = borrowingRecordService.getBorrowingRecordById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(record, result.getBody());
    }

    @Test
    void testCreateBorrowingRecord() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record = new BorrowingRecord(1L, customer, book, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));

        when(borrowingRecordService.createBorrowingRecord(any(BorrowingRecord.class))).thenReturn(new ResponseEntity<>(record, HttpStatus.CREATED));

        ResponseEntity<BorrowingRecord> response = borrowingRecordController.createBorrowingRecord(record);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(record, response.getBody());
    }

    @Test
    void testDeleteBorrowingRecord() {
        when(borrowingRecordService.deleteBorrowingRecord(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> response = borrowingRecordController.deleteBorrowingRecord(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSearchBorrowingRecordsByCustomerId() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record = new BorrowingRecord(1L, customer, book, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));
        List<BorrowingRecord> records = Arrays.asList(record);

        when(borrowingRecordService.getBorrowingRecordsByCustomerId(anyLong())).thenReturn(new ResponseEntity<>(records, HttpStatus.OK));

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.getBorrowingRecords(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }

    @Test
    void testSearchBorrowingRecordsByBookId() {
        Customer customer = new Customer(1L, "Omar Tayfour", "omartayfour@example.com", "01012345678", "password1", "Alexandria", null);
        Author author1 = new Author(1L, "Author One", LocalDate.of(1990, 1, 1), "Nationality1", null);
        Book book = new Book(1L, "Book Title 1", author1, "978-3-16-148410-0", LocalDate.of(2000, 1, 1), "Genre 1", true);
        BorrowingRecord record = new BorrowingRecord(1L, customer, book, LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 26));
        List<BorrowingRecord> records = Arrays.asList(record);

        when(borrowingRecordService.getBorrowingRecordsByBookId(anyLong())).thenReturn(new ResponseEntity<>(records, HttpStatus.OK));

        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.getBorrowingRecords(null, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }

    @Test
    void testSearchBorrowingRecordsBadRequest() {
        ResponseEntity<List<BorrowingRecord>> response = borrowingRecordController.getBorrowingRecords(null, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
