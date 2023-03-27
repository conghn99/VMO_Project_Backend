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
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "apartment_num", nullable = false, unique = true)
    private String apartmentNumber;

    @Column(name = "area", nullable = false)
    private Double area;

    @Column(name = "num_rooms", nullable = false)
    private Integer numberOfRooms;

    @Column(name = "apartment_status")
    private boolean status;

    @OneToMany(mappedBy = "apartment")
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "apartment")
    private List<Person> persons = new ArrayList<>();

}
