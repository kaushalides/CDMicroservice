package com.learnjavawithkaushi.cdpatientmicroservice.controller;

import com.learnjavawithkaushi.cdpatientmicroservice.exception.PatientNotFoundException;
import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import com.learnjavawithkaushi.cdpatientmicroservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Slf4j
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        log.info("GET /api/patients - Fetching all patients");
        List<Patient> patients = service.getAll();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable String id) {
        log.info("GET /api/patients/{} - Fetching patient by id", id);
        return service.getById(id)
                .map(patient -> {
                    log.info("Found patient: {} {}", patient.getFirstName(), patient.getLastName());
                    return ResponseEntity.ok(patient);
                })
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Patient> create(@Valid @RequestBody Patient patient) {
        log.info("POST /api/patients - Creating new patient");
        Patient createdPatient = service.create(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> update(@PathVariable String id, @Valid @RequestBody Patient patient) {
        log.info("PUT /api/patients/{} - Updating patient", id);
        Patient updatedPatient = service.update(id, patient);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("DELETE /api/patients/{} - Deleting patient", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        long count = service.count();
        return ResponseEntity.ok("Patient Service is running. Total patients: " + count);
    }
}