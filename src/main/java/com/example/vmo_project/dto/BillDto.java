package com.example.vmo_project.dto;

import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.entity.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long id;
    private Double electricityNumber;
    private Double waterNumber;
    private LocalDateTime paidDate;
    private boolean status;
    private Long apartmentId;
    private List<String> feeTypeList = new ArrayList<>();

    public BillDto(Bill entity) {
        this.setId(entity.getId());
        this.electricityNumber = entity.getElectricityNumber();
        this.waterNumber = entity.getWaterNumber();
        this.paidDate = entity.getPaidDate();
        this.status = entity.isStatus();
        this.apartmentId = entity.getApartment().getId();
        this.feeTypeList = entity.getFeeTypes().stream().map(FeeType::getName).toList();
    }
}
