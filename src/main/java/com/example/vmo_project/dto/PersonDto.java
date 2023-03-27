package com.example.vmo_project.dto;

import com.example.vmo_project.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String cardIdNumber;
    private LocalDate birthDate;
    private boolean gender;
    private boolean representative;
    private Long apartmentId;

    public PersonDto(Person entity) {
        this.setId(entity.getId());
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.cardIdNumber = entity.getCardIdNumber();
        this.birthDate = entity.getBirthDate();
        this.gender = entity.isGender();
        this.representative = entity.isRepresentative();
        this.apartmentId = entity.getApartment().getId();
    }
}
