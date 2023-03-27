package com.example.vmo_project.dto;

import com.example.vmo_project.entity.FeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeTypeDto {
    private Long id;
    private String name;
    private Double price;
    private List<BillDto> bills = new ArrayList<>();

    public FeeTypeDto(FeeType entity) {
        this.setId(entity.getId());
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.bills = entity.getBills().stream().map(BillDto::new).toList();
    }
}
