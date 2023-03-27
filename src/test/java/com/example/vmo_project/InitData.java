package com.example.vmo_project;

import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.entity.User;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.repository.UserRepository;
import org.hibernate.mapping.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;

@SpringBootTest
public class InitData {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FeeTypeRepository feeTypeRepository;

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
}
