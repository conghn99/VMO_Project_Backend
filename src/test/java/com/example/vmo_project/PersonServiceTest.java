package com.example.vmo_project;

import com.example.vmo_project.constant.ConstantDateFormat;
import com.example.vmo_project.constant.ConstantError;
import com.example.vmo_project.dto.PersonDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertPersonRequest;
import com.example.vmo_project.request.UpdatePersonRequest;
import com.example.vmo_project.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGetAllAndGetByIdPerson() {
        // Arrange
        Person person1 = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, new Apartment());
        Person person2 = new Person(2L, "Long", "long@gmail.com", "0987654321", "98765432", LocalDate.parse("10-10-1999", ConstantDateFormat.FORMATTER), true, false, new Apartment());
        Person person3 = new Person(3L, "Hoang", "hoang@gmail.com", "0967584932", "123874565", LocalDate.parse("12-12-1999", ConstantDateFormat.FORMATTER), true, true, new Apartment());
        Person person4 = new Person(4L, "Dung", "dung@gmail.com", "0919283746", "198723746", LocalDate.parse("09-09-1999", ConstantDateFormat.FORMATTER), true, false, null);
        Person person5 = new Person(5L, "Quy", "quy@gmail.com", "09912387645", "832467324", LocalDate.parse("08-08-1999", ConstantDateFormat.FORMATTER), true, true, null);

        List<Person> persons = Arrays.asList(person1, person2, person3, person4, person5);

        Page<Person> page = new PageImpl<>(persons, PageRequest.of(0, 10, Sort.by("name")), 5);
        when(personRepository.findAll(PageRequest.of(0, 10, Sort.by("name"))))
                .thenReturn(page);
        when(personRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(persons.get(0)));
        when(personRepository.findById(2L))
                .thenReturn(java.util.Optional.ofNullable(persons.get(1)));
        when(personRepository.findById(3L))
                .thenReturn(java.util.Optional.ofNullable(persons.get(2)));
        when(personRepository.findById(4L))
                .thenReturn(java.util.Optional.ofNullable(persons.get(3)));
        when(personRepository.findById(5L))
                .thenReturn(java.util.Optional.ofNullable(persons.get(4)));

        // Act
        Page<PersonDto> result = personService.getAll(0);
        PersonDto personDto1 = personService.getById(1L);
        PersonDto personDto2 = personService.getById(2L);
        PersonDto personDto3 = personService.getById(3L);
        PersonDto personDto4 = personService.getById(4L);
        PersonDto personDto5 = personService.getById(5L);

        // Assert
        assertEquals(5, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(5, result.getContent().size());
        List<String> names = result.getContent().stream().map(PersonDto::getName).toList();
        assertIterableEquals(List.of("Kong", "Long", "Hoang", "Dung", "Quy"), names);

        assertNotNull(personDto1);
        assertNotNull(personDto3);
        assertNotNull(personDto5);
        assertNotNull(personDto4);
        assertEquals(personDto1.getName(), result.getContent().get(0).getName());
        assertEquals(personDto2.getEmail(), result.getContent().get(1).getEmail());
        assertEquals(personDto2.getApartment(), result.getContent().get(1).getApartment());
        assertEquals(personDto3.getCardIdNumber(), result.getContent().get(2).getCardIdNumber());
        assertEquals(personDto4.getPhoneNumber(), result.getContent().get(3).getPhoneNumber());
        assertEquals(personDto5.isGender(), result.getContent().get(4).isGender());
        assertTrue(personDto5.isGender());
        assertFalse(personDto4.isRepresentative());
        assertThrows(NotFoundException.class, () -> personService.getById(6L));
    }

    @Test
    void testGetAllPersonByNullOrIdApartment() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Person person1 = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        Person person2 = new Person(2L, "Long", "long@gmail.com", "0987654321", "98765432", LocalDate.parse("10-10-1999", ConstantDateFormat.FORMATTER), true, false, apartment1);
        Person person3 = new Person(3L, "Hoang", "hoang@gmail.com", "0967584932", "123874565", LocalDate.parse("12-12-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        Person person4 = new Person(4L, "Dung", "dung@gmail.com", "0919283746", "198723746", LocalDate.parse("09-09-1999", ConstantDateFormat.FORMATTER), true, false, null);
        Person person5 = new Person(5L, "Quy", "quy@gmail.com", "09912387645", "832467324", LocalDate.parse("08-08-1999", ConstantDateFormat.FORMATTER), true, true, null);

        when(personRepository.findAllByApartmentIdOrApartmentIsNull(1L))
                .thenReturn(Arrays.asList(person1, person2, person3, person4, person5));
        when(personRepository.findByNameOrEmailOrApartment("ong", null))
                .thenReturn(Arrays.asList(person1, person2));
        when(personRepository.findByNameOrEmailOrApartment("quy@", null))
                .thenReturn(Arrays.asList(person5));

        // Act
        List<PersonDto> result = personService.getAllNonActiveOrByApartmentId(1L);
        List<PersonDto> resultSearch1 = personService.getByKeyword("ong", null);
        List<PersonDto> resultSearch2 = personService.getByKeyword("quy@", null);

        // Assert
        assertNotNull(resultSearch1);
        assertNotNull(resultSearch2);
        assertEquals(5, result.size());
        assertEquals(2, resultSearch1.size());
        assertEquals(1, resultSearch2.size());
        List<String> names = result.stream().map(PersonDto::getName).toList();
        assertIterableEquals(List.of("Kong", "Long", "Hoang", "Dung", "Quy"), names);
    }

    @Test
    void testAddPerson() {
        // Arrange
        InsertPersonRequest request = new InsertPersonRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPhoneNumber("123456789");
        request.setCardIdNumber("12345");
        request.setBirthDate("01-01-2000");
        request.setGender(true);
        request.setRepresentative(false);
        request.setApartmentId(1L);

        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        when(apartmentRepository.findById(1L))
                .thenReturn(Optional.of(apartment));

        // Act
        PersonDto result = personService.add(request);

        // Assert
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        assertEquals("123456789", result.getPhoneNumber());
        assertEquals("12345", result.getCardIdNumber());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthDate());
        assertTrue(result.isGender());
        assertFalse(result.isRepresentative());
        assertEquals("101", result.getApartment().getApartmentNumber());
        assertEquals(10D, result.getApartment().getArea());
        assertEquals(3, result.getApartment().getNumberOfRooms());
        assertTrue(result.getApartment().isStatus());
    }

    @Test
    void throwsNotFoundExceptionWhenApartmentNotFound() {
        // Arrange
        InsertPersonRequest request = new InsertPersonRequest();
        request.setName("Alice");
        request.setEmail("alice@example.com");
        request.setPhoneNumber("123456789");
        request.setCardIdNumber("12345");
        request.setBirthDate("01-01-2000");
        request.setGender(true);
        request.setRepresentative(false);
        request.setApartmentId(1L);
        when(apartmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> personService.add(request));

        // Assert
        assertEquals(ConstantError.APARTMENT_NOT_FOUND + 1L, exception.getMessage());
    }

    @Test
    void testUpdatePersonWithNullApartment() {
        // Arrange
        Long personId = 1L;
        UpdatePersonRequest request = new UpdatePersonRequest();
        request.setRepresentative(true);
        request.setApartmentId(0L);

        Person person = new Person();
        person.setId(personId);
        person.setRepresentative(false);

        when(personRepository.findById(personId))
                .thenReturn(Optional.of(person));

        // Act
        PersonDto updatedPersonDto = personService.update(personId, request);

        // Assert
        assertEquals(request.isRepresentative(), updatedPersonDto.isRepresentative());
        assertNull(updatedPersonDto.getApartment());
        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, times(1)).save(person);
        verify(apartmentRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdatePersonWithNonNullApartment() {
        // Arrange
        Long personId = 1L;
        Long apartmentId = 2L;
        UpdatePersonRequest request = new UpdatePersonRequest();
        request.setRepresentative(false);
        request.setApartmentId(apartmentId);

        Person person = new Person();
        person.setId(personId);
        person.setRepresentative(true);

        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        person.setApartment(apartment);

        when(personRepository.findById(personId))
                .thenReturn(Optional.of(person));
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.of(apartment));

        // Act
        PersonDto updatedPersonDto = personService.update(personId, request);

        // Assert
        assertEquals(request.isRepresentative(), updatedPersonDto.isRepresentative());
        assertEquals(apartment.getApartmentNumber(), updatedPersonDto.getApartment().getApartmentNumber());
        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, times(1)).save(person);
        verify(apartmentRepository, times(1)).findById(apartmentId);
    }

    @Test
    void testUpdatePersonNotFound() {
        // Arrange
        Long personId = 1L;
        UpdatePersonRequest request = new UpdatePersonRequest();
        request.setRepresentative(true);
        request.setApartmentId(0L);

        when(personRepository.findById(personId))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(NotFoundException.class, () -> {
            personService.update(personId, request);
        }, ConstantError.PERSON_NOT_FOUND + personId);

        // Assert
        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, never()).save(any(Person.class));
        verify(apartmentRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdatePersonApartmentNotFound() {
        // Arrange
        Long personId = 1L;
        Long apartmentId = 2L;
        UpdatePersonRequest request = new UpdatePersonRequest();
        request.setRepresentative(false);
        request.setApartmentId(apartmentId);

        Person person = new Person();
        person.setId(personId);
        person.setRepresentative(true);

        when(personRepository.findById(personId))
                .thenReturn(Optional.of(person));
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(NotFoundException.class, () -> {
            personService.update(personId, request);
        }, ConstantError.APARTMENT_NOT_FOUND + apartmentId);

        // Assert
        verify(personRepository, times(1)).findById(personId);
        verify(personRepository, never()).save(any(Person.class));
        verify(apartmentRepository, times(1)).findById(apartmentId);
    }
}
