package com.vodafone.library.controller;

import com.vodafone.library.model.Author;
import com.vodafone.library.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RestController
@RequestMapping("/api/authors")
@Tag(name = "Author", description = "API for author management")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the authors",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))})
    })
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @Operation(summary = "Get author by ID", description = "Retrieve an author by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the author",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @Operation(summary = "Create a new author", description = "Create a new author with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the new author to be created",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"name\": \"Omar Tayfour\", \"birthDate\": \"1998-11-28\", \"nationality\": \"Egyptian\"}"))
    )
    @PostMapping
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    @Operation(summary = "Update an author", description = "Update an existing author by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the author to be updated",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"name\": \"Omar Tayfour\", \"birthDate\": \"1998-11-28\", \"nationality\": \"Egyptian\"}"))
    )
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author) {
        return authorService.updateAuthor(id, author);
    }


    @Operation(summary = "Delete an author", description = "Delete an author by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        return authorService.deleteAuthor(id);
    }

}
