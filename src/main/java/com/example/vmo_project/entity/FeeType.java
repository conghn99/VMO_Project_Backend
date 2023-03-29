package com.example.vmo_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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

    @Column(name = "type", unique = true, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @JsonBackReference
    @ManyToMany(mappedBy = "feeTypes")
    private List<Bill> bills;
}
