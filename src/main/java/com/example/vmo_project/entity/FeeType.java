package com.example.vmo_project.entity;

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

    @ManyToMany
    @JoinTable(name = "fee_type_bills",
            joinColumns = @JoinColumn(name = "feeType_id"),
            inverseJoinColumns = @JoinColumn(name = "bills_id"))
    private List<Bill> bills = new ArrayList<>();

}
