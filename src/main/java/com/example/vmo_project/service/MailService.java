package com.example.vmo_project.service;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(Person person, Apartment apartment, Bill bill, double amount) {
        SimpleMailMessage message = new SimpleMailMessage();

        StringBuilder subjectBuilder = new StringBuilder("Thông báo đóng tiền phí sinh hoạt tháng ");
        String subject = subjectBuilder.append(bill.getBillDate().getMonthValue())
                .append("/")
                .append(bill.getBillDate().getYear())
                .append(" của căn hộ ")
                .append(apartment.getApartmentNumber())
                .toString();

        StringBuilder bodyBuilder = new StringBuilder("Kính gửi ");
        String body = bodyBuilder.append(person.getName())
                .append(",\n\n")
                .append("Thông tin chi tiết các loại phí:\n")
                .append("- Tiền điện:\n")
                .append(" .Số điện tiêu thụ: ")
                .append(bill.getElectricityNumber())
                .append("Kwh\n")
                .append(" .Tổng hóa đơn điện: ")
                .append(bill.getElectricityNumber()*bill.getFeeTypes().stream().filter(feeType -> feeType.getName().equals("electricity")).findFirst().orElse(null).getPrice())
                .append(" VNĐ\n")
                .append("- Tiền nước:\n")
                .append(" .Số nước tiêu thụ: ")
                .append(bill.getWaterNumber())
                .append("m3\n")
                .append(" .Tổng hóa đơn nước: ")
                .append(bill.getWaterNumber()*bill.getFeeTypes().stream().filter(feeType -> feeType.getName().equals("water")).findFirst().orElse(null).getPrice())
                .append(" VNĐ\n")
                .append("- Tiền chi phí chung cư:\n")
                .append(" .Tiền gửi xe: ")
                .append(bill.getFeeTypes().stream().filter(feeType -> feeType.getName().equals("parking")).findFirst().orElse(null).getPrice())
                .append(" VNĐ\n")
                .append(" .Tiền vệ sinh: ")
                .append(bill.getFeeTypes().stream().filter(feeType -> feeType.getName().equals("cleaning")).findFirst().orElse(null).getPrice())
                .append(" VNĐ\n")
                .append(" .Tiền bảo trì: ")
                .append(bill.getFeeTypes().stream().filter(feeType -> feeType.getName().equals("maintaining")).findFirst().orElse(null).getPrice())
                .append(" VNĐ\n")
                .append("Tổng số tiền phải thanh toán: ")
                .append(amount)
                .append(" VNĐ\n\n")
                .append("Trân trọng,\nBan quản lý tòa nhà")
                .toString();

        message.setTo(person.getEmail());
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }
}
