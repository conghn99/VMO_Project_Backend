package com.example.vmo_project.repository;

import com.example.vmo_project.entity.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeType, Long> {
    List<FeeType> findByIdIn(List<Long> ids);
}
