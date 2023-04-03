package com.example.vmo_project.dto;

import com.example.vmo_project.entity.Apartment;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.entity.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate billDate;
    private LocalDate paidDate;
    private boolean status;
    private Apartment apartment;
    private List<FeeType> feeTypeList = new ArrayList<>();

    public BillDto(Bill entity) {
        this.setId(entity.getId());
        this.electricityNumber = entity.getElectricityNumber();
        this.waterNumber = entity.getWaterNumber();
        this.billDate = entity.getBillDate();
        this.paidDate = entity.getPaidDate();
        this.status = entity.isStatus();
        this.apartment = entity.getApartment();
        this.feeTypeList = entity.getFeeTypes();
    }
}
