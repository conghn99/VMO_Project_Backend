package com.example.vmo_project;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.entity.User;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.repository.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class InitData {
    @Autowired
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Rollback(value = false)
    void save_user() {
        User user = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("111"))
                .build();
        userRepository.save(user);
    }

    @Test
    @Rollback(value = false)
    void save_fee() {
        FeeType feeType1 = FeeType.builder()
                .name("electricity")
                .price(3000D)
                .build();
        FeeType feeType2 = FeeType.builder()
                .name("water")
                .price(7000D)
                .build();
        FeeType feeType3 = FeeType.builder()
                .name("parking")
                .price(50000D)
                .build();
        FeeType feeType4 = FeeType.builder()
                .name("cleaning")
                .price(25000D)
                .build();
        FeeType feeType5 = FeeType.builder()
                .name("maintaining")
                .price(40000D)
                .build();
        feeTypeRepository.saveAll(Arrays.asList(feeType1, feeType2, feeType3, feeType4, feeType5));
    }

    @Test
    @Rollback(value = false)
    void save_person() {
        Random rd = new Random();
        List<Apartment> apartmentList = apartmentRepository.findAll();
        for (int i = 0; i < 20; i++) {
            Apartment apartment = apartmentList.get(rd.nextInt(apartmentList.size()));
            Person person = Person.builder()
                    .name(faker.name().fullName())
                    .email(faker.internet().emailAddress())
                    .phoneNumber(faker.phoneNumber().cellPhone())
                    .cardIdNumber(faker.idNumber().valid())
                    .birthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .gender(faker.bool().bool())
                    .representative(false)
                    .apartment(apartment)
                    .build();
            personRepository.save(person);
        }
    }
}
