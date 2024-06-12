package com.vodafone.library.controller;


import com.vodafone.library.model.BorrowingRecord;
import com.vodafone.library.services.BorrowingRecordService;
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
@RequestMapping("/api/borrowings")
@Tag(name = "BorrowingRecord", description = "API for borrowing record management")
public class BorrowingRecordController {
    @Autowired
    private BorrowingRecordService borrowingRecordService;


    @Operation(summary = "Get all borrowing records", description = "Retrieve a list of all borrowing records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the borrowing records",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BorrowingRecord.class))})
    })
    @GetMapping
    public ResponseEntity<List<BorrowingRecord>> getAllBorrowingRecords() {
        return borrowingRecordService.getAllBorrowingRecords();
    }

    @Operation(summary = "Get borrowing record by ID", description = "Retrieve a borrowing record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the borrowing record",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BorrowingRecord.class))}),
            @ApiResponse(responseCode = "404", description = "Borrowing record not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BorrowingRecord> getBorrowingRecordById(@Parameter(description = "ID of the borrowing record to be retrieved") @PathVariable Long id) {
        return borrowingRecordService.getBorrowingRecordById(id);
    }

    @Operation(summary = "Create a new borrowing record", description = "Create a new borrowing record with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Borrowing record created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BorrowingRecord.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the new borrowing record to be created",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"customerId\": 1, \"bookId\": 1, \"borrowDate\": \"2024-06-12\", \"returnDate\": \"2024-06-26\"}"))
    )
    @PostMapping
    public ResponseEntity<BorrowingRecord> createBorrowingRecord(@Valid @RequestBody BorrowingRecord borrowingRecord) {
        return borrowingRecordService.createBorrowingRecord(borrowingRecord);
    }

    @Operation(summary = "Update a borrowing record", description = "Update an existing borrowing record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowing record updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BorrowingRecord.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Borrowing record not found",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the borrowing record to be updated",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"customerId\": 1, \"bookId\": 1, \"borrowDate\": \"2024-06-12\", \"returnDate\": \"2024-06-26\"}"))
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrowingRecord(@Parameter(description = "ID of the borrowing record to be updated") @PathVariable Long id,
                                                   @Valid @RequestBody BorrowingRecord borrowingRecord) {
        return borrowingRecordService.updateBorrowingRecord(id, borrowingRecord);
    }

    @Operation(summary = "Delete a borrowing record", description = "Delete a borrowing record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrowing record deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Borrowing record not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrowingRecord(
            @Parameter(description = "ID of the borrowing record to be deleted") @PathVariable Long id) {
        return borrowingRecordService.deleteBorrowingRecord(id);
    }

    @Operation(summary = "Search borrowing records", description = "Search borrowing records by customer ID or book ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the borrowing records",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BorrowingRecord.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @GetMapping("/search")
    public ResponseEntity<List<BorrowingRecord>> getBorrowingRecords(
            @Parameter(description = "ID of the customer to search borrowing records for") @RequestParam(required = false) Long customerId,
            @Parameter(description = "ID of the book to search borrowing records for") @RequestParam(required = false) Long bookId) {
        if (customerId != null) {
            return borrowingRecordService.getBorrowingRecordsByCustomerId(customerId);
        } else if (bookId != null) {
            return borrowingRecordService.getBorrowingRecordsByBookId(bookId);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
