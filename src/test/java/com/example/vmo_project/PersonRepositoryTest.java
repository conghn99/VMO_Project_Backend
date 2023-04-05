package com.example.vmo_project;

import com.example.vmo_project.constant.ConstantDateFormat;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.PersonRepository;
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
public class PersonRepositoryTest {
    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void testFindAllByApartmentIdOrApartmentIsNull() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Apartment apartment2 = new Apartment(2L, "102", 11D, 4, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.saveAll(List.of(apartment1, apartment2));
        Person person1 = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, apartmentRepository.findAll().get(0));
        Person person2 = new Person(2L, "Long", "long@gmail.com", "0987654321", "98765432", LocalDate.parse("10-10-1999", ConstantDateFormat.FORMATTER), true, false, apartmentRepository.findAll().get(0));
        Person person3 = new Person(3L, "Hoang", "hoang@gmail.com", "0967584932", "123874565", LocalDate.parse("12-12-1999", ConstantDateFormat.FORMATTER), true, true, apartmentRepository.findAll().get(1));
        Person person4 = new Person(4L, "Dung", "dung@gmail.com", "0919283746", "198723746", LocalDate.parse("09-09-1999", ConstantDateFormat.FORMATTER), true, false, null);
        Person person5 = new Person(5L, "Quy", "quy@gmail.com", "09912387645", "832467324", LocalDate.parse("08-08-1999", ConstantDateFormat.FORMATTER), true, true, null);
        personRepository.saveAll(List.of(person1, person2, person3, person4, person5));

        // Test
        List<Person> result = personRepository.findAllByApartmentIdOrApartmentIsNull(apartment1.getId());

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(person1.getName(), result.get(0).getName());
        assertEquals(person2.getEmail(), result.get(1).getEmail());
        assertEquals(person5.getPhoneNumber(), result.get(3).getPhoneNumber());
        assertNotEquals(person3.getCardIdNumber(), result.get(0).getCardIdNumber());
        assertNotEquals(person3.getCardIdNumber(), result.get(1).getCardIdNumber());
        assertNotEquals(person3.getCardIdNumber(), result.get(2).getCardIdNumber());
        assertNotEquals(person3.getCardIdNumber(), result.get(3).getCardIdNumber());
    }

    @Test
    void testFindByIdIn() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment1);
        Person person1 = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        Person person2 = new Person(2L, "Long", "long@gmail.com", "0987654321", "98765432", LocalDate.parse("10-10-1999", ConstantDateFormat.FORMATTER), true, false, apartment1);
        Person person3 = new Person(3L, "Hoang", "hoang@gmail.com", "0967584932", "123874565", LocalDate.parse("12-12-1999", ConstantDateFormat.FORMATTER), true, true, null);
        Person person4 = new Person(4L, "Dung", "dung@gmail.com", "0919283746", "198723746", LocalDate.parse("09-09-1999", ConstantDateFormat.FORMATTER), true, false, null);
        Person person5 = new Person(5L, "Quy", "quy@gmail.com", "09912387645", "832467324", LocalDate.parse("08-08-1999", ConstantDateFormat.FORMATTER), true, true, null);
        personRepository.saveAll(List.of(person1, person2, person3, person4, person5));

        // Test
        List<Person> result = personRepository.findByIdIn(List.of(1L, 2L, 3L, 6L));

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testFindByNameOrEmailOrApartment() {
        // Arrange
        Apartment apartment1 = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        apartmentRepository.save(apartment1);
        Person person1 = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        Person person2 = new Person(2L, "Long", "long@gmail.com", "0987654321", "98765432", LocalDate.parse("10-10-1999", ConstantDateFormat.FORMATTER), true, false, apartment1);
        Person person3 = new Person(3L, "Hoang", "hoang@gmail.com", "0967584932", "123874565", LocalDate.parse("12-12-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        Person person4 = new Person(4L, "Dung", "dung@gmail.com", "0919283746", "198723746", LocalDate.parse("09-09-1999", ConstantDateFormat.FORMATTER), true, false, apartment1);
        Person person5 = new Person(5L, "Quy", "quy@gmail.com", "09912387645", "832467324", LocalDate.parse("08-08-1999", ConstantDateFormat.FORMATTER), true, true, apartment1);
        personRepository.saveAll(List.of(person1, person2, person3, person4, person5));

        // Test
        List<Person> result1 = personRepository.findByNameOrEmailOrApartment("ong");
        List<Person> result2 = personRepository.findByNameOrEmailOrApartment("quy@");
        List<Person> result3 = personRepository.findByNameOrEmailOrApartment("101");
        List<Person> result4 = personRepository.findByNameOrEmailOrApartment("abc");

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
        assertEquals(2, result1.size());
        assertEquals(1, result2.size());
        assertEquals(5, result3.size());
        assertEquals(0, result4.size());
    }
}
