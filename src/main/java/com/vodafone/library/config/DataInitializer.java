package com.vodafone.library.config;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.model.BorrowingRecord;
import com.vodafone.library.model.Customer;
import com.vodafone.library.repository.AuthorRepository;
import com.vodafone.library.repository.BookRepository;
import com.vodafone.library.repository.BorrowingRecordRepository;
import com.vodafone.library.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public int initDummyData() {

        for (int i = 1; i <= 5; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            author.setBirthDate(LocalDate.of(1980 + i, 1, 1));
            author.setNationality("Nationality " + i);
            authorRepository.save(author);
        }


        for (int i = 1; i <= 10; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setIsbn("ISBN" + i);
            book.setPublicationDate(LocalDate.of(2000 + i, 1, 1));
            book.setGenre("Genre " + i);
            book.setAvailable(true);
            Author author = authorRepository.findById((long) (i + 1) / 2).orElseThrow();
            book.setAuthor(author);
            bookRepository.save(book);
        }


        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setName("Customer " + i);
            customer.setEmail("customer" + i + "@example.com");
            customer.setAddress("Address " + i);
            customer.setPhoneNumber("0101234567" + i);
            String encryptedPassword = SecurityConfig.encodePassword("password" + i);
            customer.setPassword(encryptedPassword);
            customerRepository.save(customer);
        }


        for (int i = 1; i <= 6; i++) {
            BorrowingRecord borrowingRecord = new BorrowingRecord();
            Customer customer = customerRepository.findById((long) (i + 1) / 2).orElseThrow();
            Book book = bookRepository.findById((long) i).orElseThrow();
            if(book.getAvailable()){
                borrowingRecord.setCustomer(customerRepository.findById((long) (i + 1) / 2).orElseThrow());
                borrowingRecord.setBook(bookRepository.findById((long) i).orElseThrow());
                borrowingRecord.setBorrowDate(LocalDate.now().minusDays(i));
                borrowingRecord.getBook().setAvailable(false);
                bookRepository.save(borrowingRecord.getBook());
                borrowingRecordRepository.save(borrowingRecord);
            }

        }

        return 0;
    }
}
