package com.example.vmo_project.service;

import com.example.vmo_project.dto.FeeTypeDto;
import com.example.vmo_project.repository.FeeTypeRepository;
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
}
