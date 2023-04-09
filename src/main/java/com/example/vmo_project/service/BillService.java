package com.example.vmo_project.service;

import com.example.vmo_project.constant.ConstantDateFormat;
import com.example.vmo_project.constant.ConstantError;
import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.entity.Person;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.BillRepository;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.repository.PersonRepository;
import com.example.vmo_project.request.InsertBillRequest;
import com.example.vmo_project.request.UpdateBillRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final ApartmentRepository apartmentRepository;
    private final FeeTypeRepository feeTypeRepository;
    private final PersonRepository personRepository;
    private final MailService mailService;

    // Lấy tất cả các hóa đơn
    public List<BillDto> getAll() {
        return billRepository.findAll().stream()
                .map(BillDto::new)
                .toList();
    }

    // Lấy hóa đơn theo id
    public BillDto getById(Long id) {
        return new BillDto(billRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
        }));
    }

    // Lấy tất cả hóa đơn theo id căn hộ
    public List<BillDto> getByApartmentId(Long apartmentId) {
        return billRepository.findAllByApartmentId(apartmentId)
                .stream()
                .map(BillDto::new)
                .toList();
    }

    // Thêm hóa đơn
    public BillDto add(InsertBillRequest request) {
        Bill bill = Bill.builder()
                .electricityNumber(request.getElectricityNumber())
                .waterNumber(request.getWaterNumber())
                .billDate(LocalDate.parse("01-"+request.getBillDate(), ConstantDateFormat.FORMATTER))
                .paidDate(null)
                .status(false)
                .apartment(apartmentRepository.findById(request.getApartmentId()).orElseThrow(() -> {
                    throw new NotFoundException(ConstantError.APARTMENT_NOT_FOUND + request.getApartmentId());
                }))
                .feeTypes(feeTypeRepository.findByIdIn(request.getFeeTypeId()))
                .build();
        billRepository.save(bill);
        return new BillDto(bill);
    }

    // Cập nhật trạng thái, ngày thanh toán hóa đơn và danh sách các phí trong hóa đơn
    public BillDto update(Long id, UpdateBillRequest request) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> {
           throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
        });
        bill.setStatus(true);
        bill.setPaidDate(LocalDate.parse(request.getPaidDate(), ConstantDateFormat.FORMATTER));
        bill.setFeeTypes(feeTypeRepository.findByIdIn(request.getFeeTypeId()));
        billRepository.save(bill);
        return new BillDto(bill);
    }

    // Xóa hóa đơn
    public void delete(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
        });
        billRepository.delete(bill);
    }

    // Lấy danh sách hóa đơn và gửi mail
//    @Scheduled(cron = "0 0 0 30 * *")
//    @Transactional
    public void getAndSendBill() {
        // Lấy danh sách hóa đơn chưa thanh toán
        List<Bill> unpaidBill = billRepository.findAll()
                .stream()
                .filter(bill -> !bill.isStatus())
                .toList();

        // Lấy danh sách đại diện căn hộ
        List<Person> representativePerson = personRepository.findAll()
                .stream()
                .filter(Person::isRepresentative)
                .toList();

        // Match người đại diện căn hộ vs hóa đơn để gửi mail
        for (Bill bill : unpaidBill) {
            for (Person person : representativePerson) {
                if (bill.getApartment().getId().equals(person.getApartment().getId())) {
                    double total = calcUnpaidBill(bill);
                    mailService.sendEmail(person, bill.getApartment(), bill, total);
                }
            }
        }
    }

//    @Scheduled(cron = "0 8 14 * * *")
//    @Transactional
    public void testSendMail() {
        List<Bill> unpaidBill = billRepository.findAll()
                .stream()
                .filter(bill -> !bill.isStatus())
                .toList();
        Person person = personRepository.findById(23L).orElse(null);
        for (Bill bill : unpaidBill) {
            if (bill.getApartment().getId().equals(person.getApartment().getId())) {
                double total = calcUnpaidBill(bill);
                mailService.sendEmail(person, bill.getApartment(), bill, total);
            }
        }
    }

    // Tính tổng phí phải trả của từng hóa đơn
    private double calcUnpaidBill(Bill unpaidBill) {
        double total = 0;
        for (FeeType feeType : unpaidBill.getFeeTypes()) {
            if (feeType.getName().equals("electricity")) {
                total += feeType.getPrice()*unpaidBill.getElectricityNumber();
            } else if (feeType.getName().equals("water")) {
                total += feeType.getPrice()*unpaidBill.getWaterNumber();
            } else {
                total += feeType.getPrice();
            }
        }
        return total;
    }
}
