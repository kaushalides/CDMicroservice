package com.learnjavawithkaushi.cdpatientmicroservice.client;

import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class PatientServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PatientServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.baseUrl = "http://localhost:8080/api/patients";
    }

    public List<Patient> getAllPatients() {
        log.info("Calling GET /api/patients");
        try {
            ResponseEntity<List<Patient>> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Patient>>() {}
            );
            log.info("Successfully retrieved {} patients", response.getBody().size());
            return response.getBody();
        } catch (Exception e) {
            log.error("Error retrieving all patients: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve patients", e);
        }
    }

    public Patient getPatientById(String id) {
        log.info("Calling GET /api/patients/{}", id);
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(
                    baseUrl + "/" + id,
                    Patient.class
            );
            log.info("Successfully retrieved patient with id: {}", id);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error retrieving patient with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to retrieve patient with id: " + id, e);
        }
    }

    public Patient createPatient(Patient patient) {
        log.info("Calling POST /api/patients");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

            ResponseEntity<Patient> response = restTemplate.postForEntity(
                    baseUrl,
                    request,
                    Patient.class
            );
            log.info("Successfully created patient with id: {}", response.getBody().getId());
            return response.getBody();
        } catch (Exception e) {
            log.error("Error creating patient: {}", e.getMessage());
            throw new RuntimeException("Failed to create patient", e);
        }
    }

    public Patient updatePatient(String id, Patient patient) {
        log.info("Calling PUT /api/patients/{}", id);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Patient> request = new HttpEntity<>(patient, headers);

            ResponseEntity<Patient> response = restTemplate.exchange(
                    baseUrl + "/" + id,
                    HttpMethod.PUT,
                    request,
                    Patient.class
            );
            log.info("Successfully updated patient with id: {}", id);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error updating patient with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update patient with id: " + id, e);
        }
    }

    public void deletePatient(String id) {
        log.info("Calling DELETE /api/patients/{}", id);
        try {
            restTemplate.delete(baseUrl + "/" + id);
            log.info("Successfully deleted patient with id: {}", id);
        } catch (Exception e) {
            log.error("Error deleting patient with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete patient with id: " + id, e);
        }
    }

    public String getHealthStatus() {
        log.info("Calling GET /api/patients/health");
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    baseUrl + "/health",
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error checking health status: {}", e.getMessage());
            throw new RuntimeException("Failed to check health status", e);
        }
    }
}