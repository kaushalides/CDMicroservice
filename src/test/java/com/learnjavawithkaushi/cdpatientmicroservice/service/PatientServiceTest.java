package com.learnjavawithkaushi.cdpatientmicroservice.service;
import com.learnjavawithkaushi.cdpatientmicroservice.exception.PatientNotFoundException;
import com.learnjavawithkaushi.cdpatientmicroservice.model.Patient;
import com.learnjavawithkaushi.cdpatientmicroservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository repository;

    @InjectMocks
    private PatientService service;

    private Patient testPatient;

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
    void getAll_ShouldReturnAllPatients() {
        when(repository.findAll()).thenReturn(List.of(testPatient));

        List<Patient> patients = service.getAll();

        assertThat(patients).hasSize(1);
        assertThat(patients.get(0)).isEqualTo(testPatient);
        verify(repository).findAll();
    }

    @Test
    void getById_WhenPatientExists_ShouldReturnPatient() {
        when(repository.findById("1")).thenReturn(Optional.of(testPatient));

        Optional<Patient> result = service.getById("1");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testPatient);
        verify(repository).findById("1");
    }

    @Test
    void getById_WhenPatientNotExists_ShouldReturnEmpty() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        Optional<Patient> result = service.getById("999");

        assertThat(result).isEmpty();
        verify(repository).findById("999");
    }

    @Test
    void create_ShouldSaveAndReturnPatient() {
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

        when(repository.save(any(Patient.class))).thenReturn(savedPatient);

        Patient result = service.create(newPatient);

        assertThat(result).isEqualTo(savedPatient);
        assertThat(result.getId()).isNotNull();
        verify(repository).save(newPatient);
    }

    @Test
    void update_WhenPatientExists_ShouldUpdateAndReturnPatient() {
        when(repository.existsById("1")).thenReturn(true);
        when(repository.save(any(Patient.class))).thenReturn(testPatient);

        Patient result = service.update("1", testPatient);

        assertThat(result).isEqualTo(testPatient);
        assertThat(result.getId()).isEqualTo("1");
        verify(repository).existsById("1");
        verify(repository).save(testPatient);
    }

    @Test
    void update_WhenPatientNotExists_ShouldThrowException() {
        when(repository.existsById("999")).thenReturn(false);

        assertThatThrownBy(() -> service.update("999", testPatient))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient not found with id: 999");

        verify(repository).existsById("999");
        verify(repository, never()).save(any());
    }

    @Test
    void delete_WhenPatientExists_ShouldDeletePatient() {
        when(repository.existsById("1")).thenReturn(true);

        service.delete("1");

        verify(repository).existsById("1");
        verify(repository).deleteById("1");
    }

    @Test
    void delete_WhenPatientNotExists_ShouldThrowException() {
        when(repository.existsById("999")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("999"))
                .isInstanceOf(PatientNotFoundException.class)
                .hasMessage("Patient not found with id: 999");

        verify(repository).existsById("999");
        verify(repository, never()).deleteById(any());
    }

    @Test
    void count_ShouldReturnTotalPatients() {
        when(repository.count()).thenReturn(10L);

        long result = service.count();

        assertThat(result).isEqualTo(10L);
        verify(repository).count();
    }
}
