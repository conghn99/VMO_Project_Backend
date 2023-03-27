package com.example.vmo_project.controller;

import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @GetMapping("")
    public ResponseEntity<?> getAllBills() {
        return ResponseEntity.ok(billService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<?> addBill(@RequestBody BillDto dto) {
        return new ResponseEntity<>(billService.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<?> updateBill(@PathVariable Long id, @RequestBody BillDto dto) {
        return ResponseEntity.ok(billService.update(id, dto));
    }
}
