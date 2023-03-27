package com.example.vmo_project.service;

import com.example.vmo_project.dto.PersonDto;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertPersonRequest;
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

    public List<PersonDto> getAllActive() {
        return personRepository.findAllByApartmentIsNotNull()
                .stream()
                .map(PersonDto::new)
                .toList();
    }

    public PersonDto add(InsertPersonRequest request) {
        Person person = Person.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .cardIdNumber(request.getCardIdNumber())
                .birthDate(LocalDate.parse(request.getBirthDate(), formatter))
                .gender(request.isGender())
                .apartment(apartmentRepository.findById(request.getApartmentId()).orElseThrow(() -> {
                    throw new NotFoundException("Not found apartment with id = " + request.getApartmentId());
                }))
                .build();
        personRepository.save(person);
        return new PersonDto(person);
    }

    public PersonDto update(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found person with id = " + id);
        });
        person.setApartment(null);
        personRepository.save(person);
        return new PersonDto(person);
    }
}
