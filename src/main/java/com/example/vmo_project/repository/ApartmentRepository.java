package com.example.vmo_project.repository;

import com.example.vmo_project.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query("select a from Apartment a where lower(a.apartmentNumber) like lower(concat('%', ?1, '%'))")
    List<Apartment> findAllByApartmentNumber(String number);

    Apartment findByArea(Double area);

    Apartment findByApartmentNumber(String a);
}
