package com.example.vmo_project.repository;

import com.example.vmo_project.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeTypeRepository extends JpaRepository<FeeType, Long> {
    List<FeeType> findByIdIn(List<Long> ids);
}
