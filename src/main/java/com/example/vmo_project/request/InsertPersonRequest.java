package com.example.vmo_project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertPersonRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String cardIdNumber;
    private String birthDate;
    private boolean gender;
    private boolean representative;
    private Long apartmentId;
}
