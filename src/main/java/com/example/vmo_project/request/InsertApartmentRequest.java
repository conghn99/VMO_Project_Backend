package com.example.vmo_project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertApartmentRequest {
    private String apartmentNumber;
    private Double area;
    private Integer numberOfRooms;
    private boolean status;
}
