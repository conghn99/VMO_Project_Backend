package com.example.vmo_project.controller;

import com.example.vmo_project.dto.BillDto;
import com.example.vmo_project.request.InsertBillRequest;
import com.example.vmo_project.request.UpdateBillRequest;
import com.example.vmo_project.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
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
    public ResponseEntity<?> addBill(@RequestBody InsertBillRequest request) {
        return new ResponseEntity<>(billService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<?> updateBill(@PathVariable Long id, @RequestBody UpdateBillRequest request) {
        return ResponseEntity.ok(billService.update(id, request));
    }
}
