package com.example.vmo_project.service;

import com.example.vmo_project.ConstantError;
import com.example.vmo_project.dto.PersonDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertPersonRequest;
import com.example.vmo_project.request.UpdatePersonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final ApartmentRepository apartmentRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Lấy tất cả cư dân còn sống trong chung cư
    public List<PersonDto> getAllActive() {
        return personRepository.findAllByApartmentIsNotNull()
                .stream()
                .map(PersonDto::new)
                .toList();
    }

    // Lấy cư dân theo id
    public PersonDto getById(Long id) {
        return new PersonDto(personRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
        }));
    }

    // Thêm cư dân
    public PersonDto add(InsertPersonRequest request) {
        Person person = Person.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .cardIdNumber(request.getCardIdNumber())
                .birthDate(LocalDate.parse(request.getBirthDate(), formatter))
                .gender(request.isGender())
                .representative(request.isRepresentative())
                .apartment(apartmentRepository.findById(request.getApartmentId()).orElseThrow(() -> {
                    throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + request.getApartmentId());
                }))
                .build();
        personRepository.save(person);
        return new PersonDto(person);
    }

    // Cập nhật đại diện và căn hộ của cư dân
    public PersonDto update(Long id, UpdatePersonRequest request) {
        Person person = personRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.PERSON_NOT_FOUND + id);
        });
        if (request.getApartmentId() == 0) {
            person.setApartment(null);
        } else {
            Apartment apartment = apartmentRepository.findById(request.getApartmentId()).orElseThrow(() -> {
                throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + request.getApartmentId());
            });
            person.setApartment(apartment);
        }
        person.setRepresentative(request.isRepresentative());
        personRepository.save(person);
        return new PersonDto(person);
    }
}
