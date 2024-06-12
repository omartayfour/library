package com.vodafone.library.services;

import com.vodafone.library.model.Author;
import com.vodafone.library.model.Book;
import com.vodafone.library.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Book> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Book> createBook(Book book) {
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    public ResponseEntity<Book> updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setAvailable(bookDetails.getAvailable());
                    book.setGenre(bookDetails.getGenre());
                    book.setTitle(bookDetails.getTitle());
                    book.setPublicationDate(bookDetails.getPublicationDate());
                    return new ResponseEntity<>(bookRepository.save(book), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> deleteBook(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    if(!book.getAvailable()){
                        return ResponseEntity.badRequest().body("Book is borrowed!");
                    }
                    bookRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }).orElse(new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<Book>> searchByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContaining(title);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> searchByAuthorId(Long id) {
        List<Book> books = bookRepository.findByAuthorId(id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    public ResponseEntity<List<Book>> searchByIsbn(String isbn) {
        List<Book> books = bookRepository.findByIsbn(isbn);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


}
