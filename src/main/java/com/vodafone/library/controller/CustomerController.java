package com.vodafone.library.controller;


import com.vodafone.library.model.Customer;
import com.vodafone.library.services.CustomerService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer", description = "API for customer management")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))})
    })
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @Parameter(description = "ID of the customer to be retrieved") @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @Operation(summary = "Create a new customer", description = "Create a new customer with provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the new customer to be created",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"name\": \"Omar Tayfour\", \"email\": \"omar@example.com\", \"address\": \"123 Main St\", \"phoneNumber\": \"01012345678\", \"password\": \"password123\"}"))
    )
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @Operation(summary = "Update a customer", description = "Update an existing customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Customer.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Details of the customer to be updated",
            required = true,
            content = @Content(schema = @Schema(example =
                    "{\"name\": \"Omar Tayfour\", \"email\": \"omar@example.com\", \"address\": \"123 Main St\", \"phoneNumber\": \"01012345678\", \"password\": \"password123\"}"))
    )
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@Parameter(description = "ID of the customer to be updated") @PathVariable Long id, @Valid @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Delete a customer", description = "Delete a customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID of the customer to be deleted") @PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}
