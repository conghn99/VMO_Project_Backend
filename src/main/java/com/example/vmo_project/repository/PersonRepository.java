package com.example.vmo_project.repository;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByApartmentIdOrApartmentIsNull(Long apartmentId);

    List<Person> findByIdIn(List<Long> ids);

    @Query("select p from Person p where ((?1 is null or lower(p.name) like lower(concat('%', ?1, '%'))) " +
            "or (?1 is null or lower(p.email) like lower(concat('%', ?1, '%')))) and " +
            "(?2 is null or p.apartment = ?2)" +
            "order by p.name asc")
    List<Person> findByNameOrEmailOrApartment(String keyword, Apartment apartment);
}
