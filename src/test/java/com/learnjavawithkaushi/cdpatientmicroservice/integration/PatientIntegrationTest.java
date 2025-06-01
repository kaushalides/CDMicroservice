package com.learnjavawithkaushi.cdpatientmicroservice.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnjavawithkaushi.cdpatientmicroservice.CdPatientMicroserviceApplication;
import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import com.learnjavawithkaushi.cdpatientmicroservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = CdPatientMicroserviceApplication.class)
@AutoConfigureMockMvc
@Testcontainers
public class PatientIntegrationTest {

    @Container
    static MongoDBContainer mongoDB = new MongoDBContainer("mongo:6.0")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDB::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void fullPatientLifecycle_ShouldWorkEndToEnd() throws Exception {
        // Create patient
        Patient patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .contactNumber("1234567890")
                .emailAddress("john.doe@gmail.com")
                .gender("Male")
                .build();

        // Test create
        String response = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andReturn().getResponse().getContentAsString();

        Patient createdPatient = objectMapper.readValue(response, Patient.class);
        String patientId = createdPatient.getId();

        // Test get by id
        mockMvc.perform(get("/api/patients/" + patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));

        // Test get all
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Test update
        patient.setLastName("Updated");
        mockMvc.perform(put("/api/patients/" + patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"));

        // Test delete
        mockMvc.perform(delete("/api/patients/" + patientId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/patients/" + patientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void healthEndpoint_ShouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/patients/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient Service is running. Total patients: 0"));
    }
}