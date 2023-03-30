package com.example.vmo_project.repository;

import com.example.vmo_project.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByApartmentId(Long apartmentId);
}
