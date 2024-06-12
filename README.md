# Library Management System

## Overview
This project is a RESTful API for managing a library system built with Spring Boot. It includes endpoints for managing authors, books, customers, and borrowing records.

## Prerequisites
- Java 17
- Maven

## Setup and Running the Project
1. Clone the repository.
2. Navigate to the project directory.
3. Run `mvn clean install` to install the dependencies.
4. Run `mvn spring-boot:run` to start the application.
5. You can simply run `main()` from LibraryApplication
5. Access the API documentation at `http://localhost:8080/swagger-ui.html`.

## Endpoints
### Authors
- GET `/authors`: Retrieve all authors.
- GET `/authors/{id}`: Retrieve an author by ID.
- POST `/authors`: Create a new author.
- PUT `/authors/{id}`: Update an existing author.
- DELETE `/authors/{id}`: Delete an author by ID.

### Books
- GET `/books`: Retrieve all books.
- GET `/books/{id}`: Retrieve a book by ID.
- POST `/books`: Create a new book.
- PUT `/books/{id}`: Update an existing book.
- DELETE `/books/{id}`: Delete a book by ID.
- GET `/books/search?title={title}`: Search for books by title.
- GET `/books/search?authorId={authorId}`: Search for books by author id.
- GET `/books/search?isbn={isbn}`: Search for books by ISBN.

### Customers
- GET `/customers`: Retrieve all customers.
- GET `/customers/{id}`: Retrieve a customer by ID.
- POST `/customers`: Create a new customer.
- PUT `/customers/{id}`: Update an existing customer.
- DELETE `/customers/{id}`: Delete a customer by ID.

### Borrowing Records
- GET `/borrowings`: Retrieve all borrowing records.
- GET `/borrowings/{id}`: Retrieve a borrowing record by ID.
- POST `/borrowings`: Create a new borrowing record.
- PUT `/borrowings/{id}`: Update an existing borrowing record.
- DELETE `/borrowings/{id}`: Delete a borrowing record by ID.
- GET `/borrowings/search?userId={userId}`: Retrieve borrowing records for a specific user.
- GET `/borrowings/search?bookId={bookId}`: Retrieve borrowing records for a specific book.