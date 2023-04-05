package com.example.vmo_project;

import com.example.vmo_project.constant.ConstantDateFormat;
import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.service.MailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MailServiceTest {
    @Test
    void testSendEmail() {
        // Arrange
        Apartment apartment = new Apartment(1L, "101", 10D, 3, true, new ArrayList<>(), new ArrayList<>());
        Person person = new Person(1L, "Kong", "kong@gmail.com", "0912345678", "12345678", LocalDate.parse("20-11-1999", ConstantDateFormat.FORMATTER), true, true, apartment);
        Bill bill = new Bill(1L, 100D, 50D, LocalDate.of(2023, 2, 1), null, false, apartment, Arrays.asList(
                new FeeType(1L,"electricity", 3000D, new ArrayList<>()),
                new FeeType(2L, "water", 2000D, new ArrayList<>()),
                new FeeType(3L, "parking", 1000D, new ArrayList<>()),
                new FeeType(4L, "cleaning", 2000D, new ArrayList<>()),
                new FeeType(5L, "maintaining", 3000D, new ArrayList<>())
        ));
        double amount = 250000.0;

        JavaMailSender emailSender = mock(JavaMailSender.class);

        // Act
        MailService emailService = new MailService(emailSender);
        emailService.sendEmail(person, apartment, bill, amount);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        verify(emailSender).send(any(SimpleMailMessage.class));
        assertEquals(person.getEmail(), message.getTo()[0]);
        assertEquals("Thông báo đóng tiền phí sinh hoạt căn hộ 101", message.getSubject());
        assertTrue(message.getText().contains("Kính gửi Kong"));
        assertTrue(message.getText().contains("Tổng số tiền phải thanh toán: 250000.0 VNĐ"));
    }
}
