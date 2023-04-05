package com.example.vmo_project.entity;

import jakarta.persistence.*;
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
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NotNull(message = "apartment number must not be null")
    @Column(name = "apartment_num", unique = true)
    private String apartmentNumber;

    @NotNull(message = "area must not be null")
    @Column(name = "area")
    private Double area;

    @NotNull(message = "number of room must not be null")
    @Column(name = "num_rooms")
    private Integer numberOfRooms;

    @Column(name = "apartment_status")
    private boolean status;

    @OneToMany(mappedBy = "apartment")
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "apartment")
    private List<Person> persons = new ArrayList<>();

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", status=" + status +
                ", bills=" + bills +
                ", persons=" + persons +
                '}';
    }
}
