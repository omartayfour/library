package com.vodafone.library.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "email", unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(name = "phoneNumber", unique = true)
    @NotBlank
    @Pattern(regexp = "^(010|011|012|015)\\d{8}$", message = "Invalid phone number")
    private String phoneNumber;

    @Column(name = "password")
    @NotBlank
    private String password;

    @Column(name = "address")
    @NotBlank
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    @JsonIdentityReference(alwaysAsId = false)
    private List<BorrowingRecord> borrowingRecords;

}
