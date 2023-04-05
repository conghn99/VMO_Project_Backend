package com.example.vmo_project.controller;

import com.example.vmo_project.request.UpdateFeePriceRequest;
import com.example.vmo_project.service.FeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fees")
public class FeeTypeController {
    @Autowired
    private FeeTypeService feeTypeService;

    @GetMapping("")
    public ResponseEntity<?> getAllFees() {
        return ResponseEntity.ok(feeTypeService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getFeeById(@PathVariable Long id) {
        return ResponseEntity.ok(feeTypeService.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateFeePrice(@PathVariable Long id, @RequestBody UpdateFeePriceRequest request) {
        return ResponseEntity.ok(feeTypeService.update(id, request));
    }
}
