package com.example.vmo_project.controller;

import com.example.vmo_project.service.FeeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fees")
public class FeeTypeController {
    @Autowired
    private FeeTypeService feeTypeService;

    @GetMapping("")
    public ResponseEntity<?> getAllFees() {
        return ResponseEntity.ok(feeTypeService.getAll());
    }
}
