package com.example.vmo_project.service;

import com.example.vmo_project.constant.ConstantError;
import com.example.vmo_project.dto.FeeTypeDto;
import com.example.vmo_project.entity.FeeType;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.FeeTypeRepository;
import com.example.vmo_project.request.UpdateFeePriceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeeTypeService {
    @Autowired
    private FeeTypeRepository feeTypeRepository;

    public List<FeeTypeDto> getAll() {
        return feeTypeRepository.findAll().stream()
                .map(FeeTypeDto::new)
                .toList();
    }

    public FeeTypeDto getById(Long id) {
        return new FeeTypeDto(feeTypeRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.FEE_TYPE_NOT_FOUND + id);
        }));
    }

    public FeeTypeDto update(Long id, UpdateFeePriceRequest request) {
        FeeType feeType = feeTypeRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(ConstantError.FEE_TYPE_NOT_FOUND + id);
        });
        feeType.setPrice(request.getPrice());
        feeTypeRepository.save(feeType);
        return new FeeTypeDto(feeType);
    }

}
