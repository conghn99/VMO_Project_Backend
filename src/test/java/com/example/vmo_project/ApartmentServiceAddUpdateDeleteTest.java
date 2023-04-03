package com.example.vmo_project;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.repository.ApartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@DataJpaTest
public class ApartmentServiceAddUpdateDeleteTest {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Test
    void testDeleteApartment() {
        apartmentRepository.save(new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>()));
        apartmentRepository.save(new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>()));

        assertEquals(2, apartmentRepository.findAll().size());
    }
}
