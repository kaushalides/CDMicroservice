package com.learnjavawithkaushi.cdpatientmicroservice.client;

import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    private PatientServiceClient client;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        client = new PatientServiceClient(restTemplate);
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
    void getAllPatients_ShouldReturnListOfPatients() {
        List<Patient> expectedPatients = Arrays.asList(testPatient);
        ResponseEntity<List<Patient>> responseEntity = new ResponseEntity<>(expectedPatients, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/patients"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);

        List<Patient> result = client.getAllPatients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testPatient);
        verify(restTemplate).exchange(
                eq("http://localhost:8080/api/patients"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        );
    }

    @Test
    void getPatientById_ShouldReturnPatient() {
        ResponseEntity<Patient> responseEntity = new ResponseEntity<>(testPatient, HttpStatus.OK);

        when(restTemplate.getForEntity(
                eq("http://localhost:8080/api/patients/1"),
                eq(Patient.class)
        )).thenReturn(responseEntity);

        Patient result = client.getPatientById("1");

        assertThat(result).isEqualTo(testPatient);
        verify(restTemplate).getForEntity(
                eq("http://localhost:8080/api/patients/1"),
                eq(Patient.class)
        );
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() {
        ResponseEntity<Patient> responseEntity = new ResponseEntity<>(testPatient, HttpStatus.CREATED);

        when(restTemplate.postForEntity(
                eq("http://localhost:8080/api/patients"),
                any(HttpEntity.class),
                eq(Patient.class)
        )).thenReturn(responseEntity);

        Patient result = client.createPatient(testPatient);

        assertThat(result).isEqualTo(testPatient);
        verify(restTemplate).postForEntity(
                eq("http://localhost:8080/api/patients"),
                any(HttpEntity.class),
                eq(Patient.class)
        );
    }

    @Test
    void updatePatient_ShouldReturnUpdatedPatient() {
        ResponseEntity<Patient> responseEntity = new ResponseEntity<>(testPatient, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/api/patients/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Patient.class)
        )).thenReturn(responseEntity);

        Patient result = client.updatePatient("1", testPatient);

        assertThat(result).isEqualTo(testPatient);
        verify(restTemplate).exchange(
                eq("http://localhost:8080/api/patients/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Patient.class)
        );
    }

    @Test
    void deletePatient_ShouldCompleteSuccessfully() {
        doNothing().when(restTemplate).delete("http://localhost:8080/api/patients/1");

        assertThatNoException().isThrownBy(() -> client.deletePatient("1"));

        verify(restTemplate).delete("http://localhost:8080/api/patients/1");
    }

    @Test
    void getHealthStatus_ShouldReturnHealthMessage() {
        String healthMessage = "Patient Service is running. Total patients: 5";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(healthMessage, HttpStatus.OK);

        when(restTemplate.getForEntity(
                eq("http://localhost:8080/api/patients/health"),
                eq(String.class)
        )).thenReturn(responseEntity);

        String result = client.getHealthStatus();

        assertThat(result).isEqualTo(healthMessage);
        verify(restTemplate).getForEntity(
                eq("http://localhost:8080/api/patients/health"),
                eq(String.class)
        );
    }

    @Test
    void getAllPatients_WhenException_ShouldThrowRuntimeException() {
        when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("Connection failed"));

        assertThatThrownBy(() -> client.getAllPatients())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to retrieve patients");
    }
}