package com.example.vmo_project.service;

import com.example.vmo_project.CONST.ConstantDateFormat;
import com.example.vmo_project.CONST.ConstantError;
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
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Scheduled(cron = "0 0 0 30 * *")
    @Transactional
    public void getAndSendBill() {
        // Lấy danh sách hóa đơn chưa thanh toán
        List<Bill> unpaidBill = billRepository.findAll()
                .stream()
                .filter(bill -> !bill.isStatus())
                .toList();

        Map<String, Double> amount = calcUnpaidBill(unpaidBill);

        // Lấy danh sách đại diện căn hộ
        List<Person> representativePerson = personRepository.findAll()
                .stream()
                .filter(Person::isRepresentative)
                .toList();

        for (Person person : representativePerson) {
            String apartment = amount.entrySet().stream()
                    .filter(a -> person.getApartment().getApartmentNumber().equals(a.getKey()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            Double total = amount.entrySet().stream()
                    .filter(a -> person.getApartment().getApartmentNumber().equals(a.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
            mailService.sendEmail(apartment, total, person.getEmail());
        }
    }

    private Map<String, Double> calcUnpaidBill(List<Bill> unpaidBill) {
        Map<String, Double> amount = new HashMap<>();

        for (Bill bill : unpaidBill) {
            double total = 0;
            for (FeeType feeType : bill.getFeeTypes()) {
                if (feeType.getName().equals("electricity")) {
                    total += feeType.getPrice()*bill.getElectricityNumber();
                } else if (feeType.getName().equals("water")) {
                    total += feeType.getPrice()*bill.getWaterNumber();
                } else {
                    total += feeType.getPrice();
                }
            }
            amount.put(bill.getApartment().getApartmentNumber(), total);
        }
        return amount;
    }
}
