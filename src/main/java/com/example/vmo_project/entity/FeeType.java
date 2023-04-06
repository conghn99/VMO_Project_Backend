package com.example.vmo_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fee_type")
public class FeeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "fee type must not blank")
    @NotNull(message = "fee type must not be null")
    @Column(name = "type", unique = true)
    private String name;

    @NotBlank(message = "fee price must not blank")
    @NotNull(message = "fee price must not be null")
    @Column(name = "price")
    private Double price;

    @JsonBackReference
    @ManyToMany(mappedBy = "feeTypes")
    private List<Bill> bills;
}
