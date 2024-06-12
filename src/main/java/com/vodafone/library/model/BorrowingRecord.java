package com.vodafone.library.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BorrowingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"password", "borrowingRecords"})
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
//    @JsonBackReference
    private Book book;

    @Column(name = "borrowDate")
    @NotNull
    private LocalDate borrowDate;

    @Column(name = "returDate")
    private LocalDate returnDate;
}
