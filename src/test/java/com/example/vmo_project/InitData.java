package com.example.vmo_project;

import com.example.vmo_project.entity.User;
import com.example.vmo_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class InitData {
    @Autowired
    private UserRepository userRepository;

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
}
