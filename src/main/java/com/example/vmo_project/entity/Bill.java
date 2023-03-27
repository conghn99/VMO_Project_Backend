package com.example.vmo_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime paidDate;

    @Column(name = "bill_status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToMany(mappedBy = "bills")
    private List<FeeType> feeTypes = new ArrayList<>();
}
