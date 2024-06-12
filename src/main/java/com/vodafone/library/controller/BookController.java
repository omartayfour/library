package com.vodafone.library.controller;

import com.vodafone.library.model.Book;
import com.vodafone.library.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "API for book management")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all books", description = "Retrieve a list of all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))})
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@Parameter(description = "ID of the book to be retrieved") @PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the new book to be created",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"title\": \"Book Title\", \"isbn\": \"978-3-16-148410-0\", \"publicationDate\": \"2001-01-01\", \"genre\": \"Genre\", \"available\": true, \"author\": {\"id\": 1}}"))
    )
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }

    @Operation(summary = "Delete a book", description = "Delete a book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@Parameter(description = "ID of the book to be deleted") @PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @Operation(summary = "Update a book", description = "Update an existing book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the book to be updated",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"title\": \"Updated Book Title\", \"isbn\": \"978-3-16-148410-0\", \"publicationDate\": \"2001-01-01\", \"genre\": \"Genre\", \"available\": true, \"author\": {\"id\": 1}}"))
    )
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @Operation(summary = "Search books", description = "Search for books by title, author ID, or ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@Parameter(description = "Title of the book") @RequestParam(required = false) String title,
                                                  @Parameter(description = "ID of the author") @RequestParam(required = false) Long authorId,
                                                  @Parameter(description = "ISBN of the book") @RequestParam(required = false) String isbn) {
        if (title != null) {
            return bookService.searchByTitle(title);
        } else if (authorId != null) {
            return bookService.searchByAuthorId(authorId);
        } else if (isbn != null) {
            return bookService.searchByIsbn(isbn);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
