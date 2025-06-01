    package com.learnjavawithkaushi.cdpatientmicroservice.controller;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.learnjavawithkaushi.cdpatientmicroservice.exception.PatientNotFoundException;
    import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
    import com.learnjavawithkaushi.cdpatientmicroservice.service.PatientService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.Mockito;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.context.TestConfiguration;
    import org.springframework.context.annotation.Bean;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.eq;
    import static org.mockito.Mockito.*;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @SpringBootTest
    @AutoConfigureMockMvc
    class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PatientService service;

        @Autowired
        private ObjectMapper objectMapper;

        private Patient testPatient;

        @TestConfiguration
        static class TestConfig {
            @Bean
            public PatientService patientService() {
                return Mockito.mock(PatientService.class);
            }
        }

        @BeforeEach
        void setUp() {
            testPatient = Patient.builder()
                    .id("1")
                    .firstName("John")
                    .lastName("Doe")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .contactNumber("1234567890")
                    .emailAddress("john.doe@gmail.com")
                    .gender("Male")
                    .build();
        }

        @Test
        void getAllPatients_ShouldReturnListOfPatients() throws Exception {
            when(service.getAll()).thenReturn(List.of(testPatient));

            mockMvc.perform(get("/api/patients"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].firstName").value("John"))
                    .andExpect(jsonPath("$[0].lastName").value("Doe"));

            verify(service).getAll();
        }

        @Test
        void getPatientById_WhenPatientExists_ShouldReturnPatient() throws Exception {
            when(service.getById("1")).thenReturn(Optional.of(testPatient));

            mockMvc.perform(get("/api/patients/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.firstName").value("John"));

            verify(service).getById("1");
        }

        @Test
        void getPatientById_WhenPatientNotExists_ShouldReturn404() throws Exception {
            when(service.getById("999")).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/patients/999"))
                    .andExpect(status().isNotFound());

            verify(service).getById("999");
        }

        @Test
        void createPatient_WithValidData_ShouldReturnCreatedPatient() throws Exception {
            Patient newPatient = Patient.builder()
                    .firstName("Jane")
                    .lastName("Smith")
                    .dateOfBirth(LocalDate.of(1985, 5, 15))
                    .contactNumber("9876543210")
                    .emailAddress("jane.smith@gmail.com")
                    .gender("Female")
                    .build();

            Patient savedPatient = Patient.builder()
                    .id("2")
                    .firstName("Jane")
                    .lastName("Smith")
                    .dateOfBirth(LocalDate.of(1985, 5, 15))
                    .contactNumber("9876543210")
                    .emailAddress("jane.smith@gmail.com")
                    .gender("Female")
                    .build();

            when(service.create(any(Patient.class))).thenReturn(savedPatient);

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newPatient)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("2"))
                    .andExpect(jsonPath("$.firstName").value("Jane"));

            verify(service).create(any(Patient.class));
        }

        @Test
        void createPatient_WithInvalidData_ShouldReturn400() throws Exception {
            Patient invalidPatient = Patient.builder()
                    .firstName("") // Invalid - empty
                    .lastName("Smith")
                    .dateOfBirth(LocalDate.of(2025, 1, 1)) // Invalid - future date
                    .contactNumber("123") // Invalid - too short
                    .emailAddress("invalid-email") // Invalid email format
                    .gender("Invalid") // Invalid gender
                    .build();

            mockMvc.perform(post("/api/patients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidPatient)))
                    .andExpect(status().isBadRequest());

            verify(service, never()).create(any(Patient.class));
        }

        @Test
        void updatePatient_WithValidData_ShouldReturnUpdatedPatient() throws Exception {
            Patient updatedPatient = Patient.builder()
                    .id("1")
                    .firstName("John")
                    .lastName("Updated")
                    .dateOfBirth(LocalDate.of(1990, 1, 1))
                    .contactNumber("1234567890")
                    .emailAddress("john.updated@gmail.com")
                    .gender("Male")
                    .build();

            when(service.update(eq("1"), any(Patient.class))).thenReturn(updatedPatient);

            mockMvc.perform(put("/api/patients/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedPatient)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.lastName").value("Updated"));

            verify(service).update(eq("1"), any(Patient.class));
        }

        @Test
        void updatePatient_WhenPatientNotExists_ShouldReturn404() throws Exception {
            when(service.update(eq("999"), any(Patient.class)))
                    .thenThrow(new PatientNotFoundException("999"));

            mockMvc.perform(put("/api/patients/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testPatient)))
                    .andExpect(status().isNotFound());

            verify(service).update(eq("999"), any(Patient.class));
        }

        @Test
        void deletePatient_WhenPatientExists_ShouldReturn204() throws Exception {
            doNothing().when(service).delete("1");

            mockMvc.perform(delete("/api/patients/1"))
                    .andExpect(status().isNoContent());

            verify(service).delete("1");
        }

        @Test
        void deletePatient_WhenPatientNotExists_ShouldReturn404() throws Exception {
            doThrow(new PatientNotFoundException("999")).when(service).delete("999");

            mockMvc.perform(delete("/api/patients/999"))
                    .andExpect(status().isNotFound());

            verify(service).delete("999");
        }

        @Test
        void healthCheck_ShouldReturnHealthStatus() throws Exception {
            when(service.count()).thenReturn(5L);

            mockMvc.perform(get("/api/patients/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Patient Service is running. Total patients: 5"));

            verify(service).count();
        }
    }
