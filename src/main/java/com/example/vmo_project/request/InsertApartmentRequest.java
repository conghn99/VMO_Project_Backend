package com.example.vmo_project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertApartmentRequest {
    private Long id;
    private String apartmentNumber;
    private Double area;
    private Integer numberOfRooms;
    private boolean status;
    private List<Long> billId;
    private List<Long> personId;
}
