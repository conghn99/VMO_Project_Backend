package com.example.vmo_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String apartment, Double total, String email) {
        SimpleMailMessage message = new SimpleMailMessage();

        StringBuilder subjectBuilder = new StringBuilder("Thông báo đóng tiền phí sinh hoạt căn hộ ");
        String subject = subjectBuilder.append(apartment).toString();

        StringBuilder bodyBuilder = new StringBuilder("Tổng phí sinh hoạt phải đóng: ");
        String body = bodyBuilder.append(total).append(" VNĐ").toString();

        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }
}
