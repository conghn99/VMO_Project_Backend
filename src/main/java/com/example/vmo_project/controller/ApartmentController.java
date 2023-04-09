package com.example.vmo_project.controller;

import com.example.vmo_project.request.InsertApartmentRequest;
import com.example.vmo_project.request.UpdateApartmentRequest;
import com.example.vmo_project.service.ApartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/apartments")
@SecurityRequirement(name = "Authorization")
public class ApartmentController {
    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("")
    public ResponseEntity<?> getAllApartment() {
        return ResponseEntity.ok(apartmentService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getApartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(apartmentService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getApartmentByNumberContain(@RequestParam String num) {
        return ResponseEntity.ok(apartmentService.getByApartmentNumber(num));
    }

    @PostMapping("")
    public ResponseEntity<?> postApartment(@RequestBody InsertApartmentRequest request) {
        return new ResponseEntity<>(apartmentService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Long id, @RequestBody UpdateApartmentRequest request) {
        return ResponseEntity.ok(apartmentService.update(id, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteApartment(@PathVariable Long id) {
        apartmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
