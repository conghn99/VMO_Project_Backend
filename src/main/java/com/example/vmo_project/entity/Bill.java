package com.example.vmo_project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "electricity_number", nullable = false)
    private Double electricityNumber;

    @Column(name = "water_number", nullable = false)
    private Double waterNumber;

    @Column(name = "payment_date")
    private LocalDate paidDate;

    @Column(name = "bill_status")
    private boolean status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToMany
    @JoinTable(name = "bill_fee_types",
            joinColumns = @JoinColumn(name = "bill_id"),
            inverseJoinColumns = @JoinColumn(name = "feeTypes_id"))
    private List<FeeType> feeTypes = new ArrayList<>();
}
