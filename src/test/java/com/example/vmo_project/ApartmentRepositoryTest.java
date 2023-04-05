package com.example.vmo_project;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.repository.ApartmentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class ApartmentRepositoryTest {
    private final Logger logger = LoggerFactory.getLogger(ApartmentRepositoryTest.class);

    @Autowired
    private ApartmentRepository apartmentRepository;

    @AfterEach
    public void setUp() {
        apartmentRepository.deleteAll();
    }

    @Test
    public void testFindByApartmentNumber() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment1);

        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment2);

        // Test
        List<Apartment> result1 = apartmentRepository.findByApartmentNumber("10");
        List<Apartment> result2 = apartmentRepository.findByApartmentNumber("101");
        List<Apartment> result3 = apartmentRepository.findByApartmentNumber("200");

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(2, result1.size());
        assertEquals(1, result2.size());
        assertEquals(0, result3.size());
    }

    @Test
    public void testFindAll() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment1);

        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment2);

        // Test
        List<Apartment> result = apartmentRepository.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment1);

        // Test
        logger.info(String.valueOf(apartmentRepository.findAll()));
        Apartment result = apartmentRepository.findByArea(apartment1.getArea());

        // Assert
        assertEquals(apartment1.getArea(), result.getArea());
    }

    @Test
    public void testSave() {
        // Arrange
        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment);

        // Test
        Apartment result = apartmentRepository.findByArea(apartment.getArea());

        // Assert
        assertEquals(apartment.getApartmentNumber(), result.getApartmentNumber());
    }

    @Test
    public void testDelete() {
        // Arrange
        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment);

        // Test
        Apartment apartmentDb = apartmentRepository.findByArea(apartment.getArea());
        apartmentRepository.delete(apartmentDb);
        List<Apartment> result = apartmentRepository.findAll();

        // Assert
        assertEquals(0, result.size());
    }
}
