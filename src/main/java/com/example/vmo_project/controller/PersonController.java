package com.example.vmo_project.controller;

import com.example.vmo_project.request.InsertPersonRequest;
import com.example.vmo_project.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("active")
    public ResponseEntity<?> getAllPersonActive() {
        return ResponseEntity.ok(personService.getAllActive());
    }

    @PostMapping("")
    public ResponseEntity<?> postPerson(@RequestBody InsertPersonRequest request) {
        return new ResponseEntity<>(personService.add(request), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> removeApartment(@PathVariable Long id) {
        return ResponseEntity.ok(personService.update(id));
    }
}
