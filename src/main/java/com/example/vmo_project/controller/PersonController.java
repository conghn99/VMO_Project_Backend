package com.example.vmo_project.controller;

import com.example.vmo_project.request.InsertPersonRequest;
import com.example.vmo_project.request.UpdatePersonRequest;
import com.example.vmo_project.service.PersonService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@SecurityRequirement(name = "Authorization")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("")
    public ResponseEntity<?> getAllPerson(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(personService.getAll(page));
    }

    @GetMapping("nonactive/{apartmentId}")
    public ResponseEntity<?> getAllPersonNonActiveOrByApartmentId(@PathVariable Long apartmentId) {
        return ResponseEntity.ok(personService.getAllNonActiveOrByApartmentId(apartmentId));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getPersonByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(personService.getByKeyword(keyword));
    }

    @PostMapping("")
    public ResponseEntity<?> postPerson(@RequestBody InsertPersonRequest request) {
        return new ResponseEntity<>(personService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateApartment(@PathVariable Long id, @RequestBody UpdatePersonRequest request) {
        return ResponseEntity.ok(personService.update(id, request));
    }
}
