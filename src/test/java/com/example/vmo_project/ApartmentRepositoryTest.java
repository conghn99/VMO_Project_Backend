package com.example.vmo_project;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.service.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class ApartmentRepositoryTest {
    @Autowired
    private ApartmentRepository apartmentRepository;

    private List<Apartment> apartmentList;

    @BeforeEach
    void initFakeData() {
        apartmentRepository.save(new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>()));
        apartmentRepository.save(new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>()));

        apartmentList = apartmentRepository.findAll();
    }

    @Test
    void testDeleteApartmentById() {
        // Arrange
        List<Apartment> apartments = apartmentList;

        // Test
        Apartment apartment = apartments.get(0);
        apartmentRepository.delete(apartment);

        // Assert
        assertEquals(1, apartmentRepository.findAll().size());
    }

    @Test
    void testFindApartmentById() {
        // Arrange
        List<Apartment> apartments = apartmentList;

        // Test
        Apartment apartment = apartmentRepository.findById(2L).orElseThrow();

        assertNotNull(apartment);
        assertEquals(apartmentRepository.findAll().get(1), apartment);
    }

    @Test
    void testFindApartmentByNotFoundId() {
        // Test
        Apartment apartment = apartmentRepository.findById(3L).orElse(null);

        // Assert
        assertNull(apartment);
    }
}
