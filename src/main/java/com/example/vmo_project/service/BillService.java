package com.example.vmo_project.service;

import com.example.vmo_project.ConstantError;
import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.ApartmentRepository;
import com.example.vmo_project.repository.BillRepository;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.request.InsertBillRequest;
import com.example.vmo_project.request.UpdateBillRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final ApartmentRepository apartmentRepository;
    private final FeeTypeRepository feeTypeRepository;

    public List<BillDto> getAll() {
        return billRepository.findAll().stream()
                .map(BillDto::new)
                .toList();
    }

    public BillDto getById(Long id) {
        return new BillDto(billRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
        }));
    }

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

    public BillDto update(Long id, UpdateBillRequest request) {
        Bill bill = billRepository.findById(id).orElseThrow(() -> {
           throw new NotFoundException(ConstantError.BILL_NOT_FOUND + id);
        });
        bill.setStatus(true);
        bill.setPaidDate(request.getPaidDate());
        billRepository.save(bill);
        return new BillDto(bill);
    }
}
