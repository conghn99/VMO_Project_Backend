package com.example.vmo_project.repository;

import com.example.vmo_project.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByApartmentIdOrApartmentIsNull(Long apartmentId);

    List<Person> findByIdIn(List<Long> ids);

    @Query("select p from Person p where lower(p.name) like lower(concat('%', ?1, '%')) " +
            "or lower(p.email) like lower(concat('%', ?1, '%')) " +
            "or lower(p.apartment.apartmentNumber) like lower(concat('%', ?1, '%'))" +
            "order by p.name asc")
    List<Person> findByNameOrEmailOrApartment(String keyword);
}
