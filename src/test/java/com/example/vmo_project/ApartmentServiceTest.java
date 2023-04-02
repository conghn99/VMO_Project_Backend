package com.example.vmo_project;

import com.example.vmo_project.dto.ApartmentDto;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.service.ApartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApartmentServiceTest {
    @Mock
    private ApartmentRepository apartmentRepository;

    @InjectMocks
    private ApartmentService apartmentService;

    @Test
    void testGetAllAndGetByIdApartments() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment3 = new Apartment(3L, "103", 12D, 5, false, new ArrayList<>(), new ArrayList<>());
        Apartment apartment4 = new Apartment(4L, "104", 13D, 6, false, new ArrayList<>(), new ArrayList<>());
        Apartment apartment5 = new Apartment(5L, "105", 14D, 7, true, new ArrayList<>(), new ArrayList<>());

        List<Apartment> apartments = Arrays.asList(apartment1, apartment2, apartment3, apartment4, apartment5);

        when(apartmentRepository.findAll()).thenReturn(apartments);
        when(apartmentRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(0)));
        when(apartmentRepository.findById(2L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(1)));
        when(apartmentRepository.findById(3L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(2)));
        when(apartmentRepository.findById(4L))
                .thenReturn(java.util.Optional.ofNullable(apartments.get(3)));
        when(apartmentRepository.findByApartmentNumber("10"))
                .thenReturn(apartments.stream().filter(a -> a.getApartmentNumber().contains("10")).toList());
        when(apartmentRepository.findByApartmentNumber("101"))
                .thenReturn(apartments.stream().filter(a -> a.getApartmentNumber().contains("101")).toList());
//        when(apartmentRepository.delete());

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

}
