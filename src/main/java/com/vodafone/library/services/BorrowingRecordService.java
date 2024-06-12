package com.vodafone.library.services;

import com.vodafone.library.model.Book;
import com.vodafone.library.model.BorrowingRecord;
import com.vodafone.library.model.Customer;
import com.vodafone.library.repository.BookRepository;
import com.vodafone.library.repository.BorrowingRecordRepository;
import com.vodafone.library.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowingRecordService {

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAll();
        return new ResponseEntity<>(borrowingRecords, HttpStatus.OK);
    }

    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(Long id) {
        return borrowingRecordRepository.findById(id)
                .map(borrowingRecord -> new ResponseEntity<>(borrowingRecord, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<BorrowingRecord> createBorrowingRecord(BorrowingRecord borrowingRecord) {
        System.out.println(borrowingRecord);
        Book book = bookRepository.findById(borrowingRecord.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!book.getAvailable()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // Book is not available
        }
        Customer customer = customerRepository.findById(borrowingRecord.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        book.setAvailable(false);
        bookRepository.save(book);

        borrowingRecord.setCustomer(customer);
        borrowingRecord.setBook(book);

        BorrowingRecord savedRecord = borrowingRecordRepository.save(borrowingRecord);
        return new ResponseEntity<>(savedRecord, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateBorrowingRecord(Long id, BorrowingRecord borrowingRecordDetails) {
        return borrowingRecordRepository.findById(id)
                .map(borrowingRecord -> {
                    Customer customer = customerRepository.findById(borrowingRecordDetails.getCustomer().getId())
                            .orElseThrow(() -> new RuntimeException("Customer not found"));
                    Book book = bookRepository.findById(borrowingRecordDetails.getBook().getId())
                            .orElseThrow(() -> new RuntimeException("Book not found"));
                    if(book.getAvailable()){
                        return ResponseEntity.badRequest().body("Book is not available for borrowing");
                    }


                    borrowingRecord.setCustomer(customer);
                    borrowingRecord.setBook(book);
                    borrowingRecord.setBorrowDate(borrowingRecordDetails.getBorrowDate());
                    borrowingRecord.setReturnDate(borrowingRecordDetails.getReturnDate());

                    BorrowingRecord updatedRecord = borrowingRecordRepository.save(borrowingRecord);
                    return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Void> deleteBorrowingRecord(Long id) {
        return borrowingRecordRepository.findById(id)
                .map(borrowingRecord -> {
                    Book book = borrowingRecord.getBook();
                    book.setAvailable(true); // Mark book as available again
                    bookRepository.save(book);

                    borrowingRecordRepository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<BorrowingRecord>> getBorrowingRecordsByCustomerId(Long customerId) {
        List<BorrowingRecord> records = borrowingRecordRepository.findByCustomerId(customerId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    public ResponseEntity<List<BorrowingRecord>> getBorrowingRecordsByBookId(Long bookId) {
        List<BorrowingRecord> records = borrowingRecordRepository.findByBookId(bookId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }
}
