package com.example.vmo_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull(message = "person name must not be null")
    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "cardID_number", unique = true)
    private String cardIdNumber;

    @NotNull(message = "birth date must not be null")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull(message = "gender must not be null")
    @Column(name = "gender")
    private boolean gender;

    @Column(name = "representative")
    private boolean representative;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
}
