package com.example.vmo_project;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.BillRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Transactional
@DataJpaTest
public class BillRepositoryTest {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @AfterEach
    public void setUp() {
        apartmentRepository.deleteAll();
    }

    @Test
    void testFindAllByApartmentId() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.saveAll(List.of(apartment1, apartment2));
        Bill bill1 = new Bill(1L, 100D, 50D, LocalDate.of(2023, 2, 1), null, false, apartmentRepository.findAll().get(0), new ArrayList<>());
        Bill bill2 = new Bill(2L, 200D, 150D, LocalDate.of(2023, 3, 1), null, false, apartmentRepository.findAll().get(1), new ArrayList<>());
        billRepository.saveAll(List.of(bill1, bill2));

        // Test
        List<Bill> result = billRepository.findAllByApartmentId(apartment1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bill1.getElectricityNumber(), result.get(0).getElectricityNumber());
    }
}
