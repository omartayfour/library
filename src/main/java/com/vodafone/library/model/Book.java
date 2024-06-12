package com.vodafone.library.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @ManyToOne
//    @JsonBackReference
    @JsonIgnoreProperties({"books"})
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "isbn", unique = true)
    @NotBlank
    private String isbn;

    @Column(name = "publicationDate")
    @NotNull
    private LocalDate publicationDate;

    @Column(name = "genre")
    @NotBlank
    private String genre;

    @Setter
    @Column(name = "available")
    @NotNull
    private boolean available = true;

    public boolean getAvailable() {
        return this.available;
    }

}
