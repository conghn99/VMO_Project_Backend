package com.example.vmo_project;

import com.example.vmo_project.constant.ConstantError;
import com.example.vmo_project.dto.ApartmentDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertApartmentRequest;
import com.example.vmo_project.request.UpdateApartmentRequest;
import com.example.vmo_project.service.ApartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApartmentServiceTest {
    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ApartmentService apartmentService;

    @Test
    void testGetAllAndGetByIdAndGetByApartmentNumberApartments() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment3 = new Apartment(3L, "103", 12D, 5, false, new ArrayList<>(), new ArrayList<>());
        Apartment apartment4 = new Apartment(4L, "104", 13D, 6, false, new ArrayList<>(), new ArrayList<>());
        Apartment apartment5 = new Apartment(5L, "105", 14D, 7, true, new ArrayList<>(), new ArrayList<>());

        List<Apartment> apartments = Arrays.asList(apartment1, apartment2, apartment3, apartment4, apartment5);

        when(apartmentRepository.findAll())
                .thenReturn(apartments);
        when(apartmentRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(0)));
        when(apartmentRepository.findById(2L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(1)));
        when(apartmentRepository.findById(3L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(2)));
        when(apartmentRepository.findById(4L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(3)));
        when(apartmentRepository.findAllByApartmentNumber("10"))
                .thenReturn(apartments.stream().filter(a -> a.getApartmentNumber().contains("10")).toList());
        when(apartmentRepository.findAllByApartmentNumber("101"))
                .thenReturn(apartments.stream().filter(a -> a.getApartmentNumber().contains("101")).toList());

        // Act
        List<ApartmentDto> apartmentDtos1 = apartmentService.getAll();
        ApartmentDto apartmentDto1 = apartmentService.getById(1L);
        ApartmentDto apartmentDto2 = apartmentService.getById(2L);
        ApartmentDto apartmentDto3 = apartmentService.getById(3L);
        ApartmentDto apartmentDto4 = apartmentService.getById(4L);
        List<ApartmentDto> apartmentDtos2 = apartmentService.getByApartmentNumber("10");
        List<ApartmentDto> apartmentDtos3 = apartmentService.getByApartmentNumber("101");

        // Assert
        assertNotNull(apartmentDtos1);
        assertNotNull(apartmentDtos2);
        assertNotNull(apartmentDtos3);
        assertEquals(5, apartmentDtos1.size());
        assertEquals(5, apartmentDtos2.size());
        assertEquals(1, apartmentDtos3.size());
        assertEquals(apartmentDto1.getApartmentNumber(), apartmentDtos1.get(0).getApartmentNumber());
        assertEquals(apartmentDto2.getArea(), apartmentDtos1.get(1).getArea());
        assertEquals(apartmentDto3.getNumberOfRooms(), apartmentDtos1.get(2).getNumberOfRooms());
        assertEquals(apartmentDto4.isStatus(), apartmentDtos1.get(3).isStatus());
        assertThrows(NotFoundException.class, () -> apartmentService.getById(6L));
    }

    @Test
    void testDeleteExistingApartment() {
        // Arrange
        Long apartmentId = 1L;
        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.of(apartment));

        // Act
        apartmentService.delete(apartmentId);

        // Assert
        verify(apartmentRepository, times(1)).delete(apartment);
    }

    @Test
    void testDeleteNonExistingApartment() {
        // Arrange
        Long apartmentId = 1L;
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.empty());

        // Act và Assert
        assertThrows(NotFoundException.class, () -> {
            apartmentService.delete(apartmentId);
        });
    }

    @Test
    void testAddValidApartment() {
        // Arrange
        InsertApartmentRequest request = new InsertApartmentRequest();
        request.setApartmentNumber("108");
        request.setArea(50.0);
        request.setNumberOfRooms(2);
        request.setStatus(true);

        Apartment apartment = new Apartment(1L, "108", 50.0, 2, true, new ArrayList<>(), new ArrayList<>());
        when(apartmentRepository.save(any(Apartment.class)))
                .thenReturn(apartment);

        // Act
        ApartmentDto result = apartmentService.add(request);

        // Assert
        verify(apartmentRepository, times(1)).save(any(Apartment.class));
        assertEquals(apartment.getApartmentNumber(), result.getApartmentNumber());
        assertEquals(apartment.getArea(), result.getArea());
        assertEquals(apartment.getNumberOfRooms(), result.getNumberOfRooms());
        assertEquals(apartment.isStatus(), result.isStatus());
    }

    @Test
    void testUpdateApartment() {
        // Arrange
        Long apartmentId = 1L;
        UpdateApartmentRequest request = new UpdateApartmentRequest();
        request.setStatus(true);
        request.setPersonId(Arrays.asList(2L, 3L));
        Person person1 = new Person();
        person1.setId(2L);
        person1.setApartment(null);
        Person person2 = new Person();
        person2.setId(3L);
        person2.setApartment(null);
        List<Person> personsToUpdate = Arrays.asList(person1, person2);
        List<Long> personIds = request.getPersonId();
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.of(new Apartment()));
        when(personRepository.findAll())
                .thenReturn(personsToUpdate);
        when(personRepository.findById(2L))
                .thenReturn(Optional.of(person1));
        when(personRepository.findById(3L))
                .thenReturn(Optional.of(person2));
        when(personRepository.findByIdIn(personIds))
                .thenReturn(personsToUpdate);
        when(personRepository.save(any(Person.class)))
                .thenReturn(new Person());

        // Act
        ApartmentDto result = apartmentService.update(apartmentId, request);

        // Assert
        verify(apartmentRepository).findById(apartmentId);
        verify(personRepository, times(personIds.size())).findById(any(Long.class));
        verify(personRepository, times(personsToUpdate.size())).save(any(Person.class));
        verify(apartmentRepository).save(any(Apartment.class));
        verify(personRepository).findByIdIn(personIds);
        assertNotNull(result);
    }

    @Test
    void testUpdateApartmentNotFound() {
        // Arrange
        Long apartmentId = 1L;
        UpdateApartmentRequest request = new UpdateApartmentRequest();
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> apartmentService.update(apartmentId, request));

        // Assert
        verify(apartmentRepository).findById(apartmentId);
        assertEquals(ConstantError.APARTMENT_NOT_FOUND + apartmentId, exception.getMessage());
    }

    @Test
    void testUpdatePersonNotFound() {
        // Arrange
        Long apartmentId = 1L;
        UpdateApartmentRequest request = new UpdateApartmentRequest();
        request.setPersonId(Arrays.asList(2L));
        Person person1 = new Person();
        person1.setId(3L);
        person1.setApartment(null);
        List<Person> personsToUpdate = Arrays.asList(person1);
        when(apartmentRepository.findById(apartmentId))
                .thenReturn(Optional.of(new Apartment()));
        when(personRepository.findAll())
                .thenReturn(personsToUpdate);
        when(personRepository.findById(2L))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> apartmentService.update(apartmentId, request));

        // Assert
        verify(apartmentRepository).findById(apartmentId);
        assertEquals(ConstantError.PERSON_NOT_FOUND + 2L, exception.getMessage());
    }
}
