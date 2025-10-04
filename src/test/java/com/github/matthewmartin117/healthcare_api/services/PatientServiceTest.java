package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepo;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
    // Create Patient
    LocalDate dob = LocalDate.of(1985, 6, 15);
        MockitoAnnotations.openMocks(this);
    testPatient = new Patient();
        testPatient.setPatientID("123");
        testPatient.setName("John Doe");
    testPatient.setDateOfBirth(dob); // example DOB
    }

    @Test
    void testCreatePatient() {
        when(patientRepo.save(testPatient)).thenReturn(testPatient);

        Patient created = patientService.createPatient(testPatient);

        assertNotNull(created);
        assertEquals("123", created.getPatientID());
        verify(patientRepo, times(1)).save(testPatient);
    }

    @Test
    void testGetPatientByID_Found() {
    when(patientRepo.findById("123")).thenReturn(Optional.of(testPatient));

    Patient found = patientService.getPatientByID("123");

    assertNotNull(found);
    assertEquals("John Doe", found.getName());
    assertEquals(LocalDate.of(1985, 6, 15), found.getDateOfBirth());
        verify(patientRepo, times(1)).findById("123");
    }

    @Test
    void testGetPatientByID_NotFound() {
        when(patientRepo.findById("999")).thenReturn(Optional.empty());

        Patient notFound = patientService.getPatientByID("999");

        assertNull(notFound);
    }

    @Test
    void testUpdatePatient() {
    when(patientRepo.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient updated = new Patient();
        updated.setName("Jane Doe");
    updated.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Patient result = patientService.updatePatient("123", updated);

        assertEquals("123", result.getPatientID());
        assertEquals("Jane Doe", result.getName());
    assertEquals(LocalDate.of(1990, 1, 1), result.getDateOfBirth());
        verify(patientRepo, times(1)).save(updated);
    }

    @Test
    void testDeletePatient() {
        doNothing().when(patientRepo).deleteById("123");

        patientService.deletePatient("123");

        verify(patientRepo, times(1)).deleteById("123");
    }

    @Test
    void testGetAllPatients() {
        List<Patient> patients = Arrays.asList(testPatient, new Patient());
        when(patientRepo.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        verify(patientRepo, times(1)).findAll();
    }
}
