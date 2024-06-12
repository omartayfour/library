package com.vodafone.library.services;

import com.vodafone.library.model.Author;
import com.vodafone.library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(authorRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Author> getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Author> createAuthor(Author author) {
        return new ResponseEntity<>(authorRepository.save(author), HttpStatus.CREATED);
    }

    public ResponseEntity<Author> updateAuthor(Long id, Author authorDetails) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorDetails.getName());
                    author.setBirthDate(authorDetails.getBirthDate());
                    author.setNationality(authorDetails.getNationality());
                    return new ResponseEntity<>(authorRepository.save(author), HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> deleteAuthor(Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    authorRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
