package com.example.vmo_project.service;

import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.entity.Bill;
import com.example.vmo_project.exception.NotFoundException;
import com.example.vmo_project.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    public List<BillDto> getAll() {
        return billRepository.findAll().stream()
                .map(BillDto::new)
                .toList();
    }

    public BillDto getById(Long id) {
        return new BillDto(billRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Not found bill with id = " + id);
        }));
    }

    public BillDto add(BillDto dto) {
        return null;
    }

    public BillDto update(Long id, BillDto dto) {
        return null;
    }
}
