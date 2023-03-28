package com.example.vmo_project.repository;

import com.example.vmo_project.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByApartmentIsNotNull();

    List<Person> findAllByApartmentIsNull();

    List<Person> findByIdIn(List<Long> ids);
}
