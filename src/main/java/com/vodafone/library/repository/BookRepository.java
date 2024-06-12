package com.vodafone.library.repository;

import com.vodafone.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String title);
    List<Book> findByAuthorId(Long authorId);
    List<Book> findByIsbn(String isbn);
}
