package com.learnjavawithkaushi.cdpatientmicroservice.service;

import com.learnjavawithkaushi.cdpatientmicroservice.exception.PatientNotFoundException;
import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import com.learnjavawithkaushi.cdpatientmicroservice.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public List<Patient> getAll() {
        log.info("Fetching all patients");
        List<Patient> patients = repository.findAll();
        log.info("Found {} patients", patients.size());
        return patients;
    }

    public Optional<Patient> getById(String id) {
        log.info("Fetching patient with id: {}", id);
        Optional<Patient> patient = repository.findById(id);
        if (patient.isEmpty()) {
            log.warn("Patient not found with id: {}", id);
        }
        return patient;
    }

    public Patient create(Patient patient) {
        log.info("Creating new patient: {} {}", patient.getFirstName(), patient.getLastName());
        patient.setId(null); // Ensure new patient doesn't have an ID
        Patient savedPatient = repository.save(patient);
        log.info("Created patient with id: {}", savedPatient.getId());
        return savedPatient;
    }

    public Patient update(String id, Patient patient) {
        log.info("Updating patient with id: {}", id);

        // Check if patient exists
        if (!repository.existsById(id)) {
            log.error("Cannot update - patient not found with id: {}", id);
            throw new PatientNotFoundException(id);
        }

        patient.setId(id);
        Patient updatedPatient = repository.save(patient);
        log.info("Updated patient with id: {}", id);
        return updatedPatient;
    }

    public void delete(String id) {
        log.info("Deleting patient with id: {}", id);

        // Check if patient exists
        if (!repository.existsById(id)) {
            log.error("Cannot delete - patient not found with id: {}", id);
            throw new PatientNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Deleted patient with id: {}", id);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.count();
    }
}