package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    @Mock
    private PatientRepository patientRepo;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testPatient = new Patient();
        testPatient.setPatientID(1L);
        testPatient.setName("John Doe");
        testPatient.setDateOfBirth(LocalDate.now());
    }

    @Test
    void testCreatePatient() {
        when(patientRepo.save(any(Patient.class))).thenReturn(testPatient);
        Patient saved = patientService.createPatient(testPatient);
        assertEquals("John Doe", saved.getName());
        verify(patientRepo, times(1)).save(testPatient);
    }

    @Test
    void testGetPatientById() {
        when(patientRepo.findById(1L)).thenReturn(Optional.of(testPatient));
        Patient found = patientService.getPatientByID(1L);
        assertEquals(1L, found.getPatientID());
    }

    @Test
    void testGetAllPatients() {
        when(patientRepo.findAll()).thenReturn(Collections.singletonList(testPatient));
        List<Patient> all = patientService.getAllPatients();
        assertEquals(1, all.size());
    }

    @Test
    void testDeletePatient() {
        patientRepo.save(testPatient); // make sure patient exists

        patientService.deletePatient(testPatient.getPatientID());

        assertFalse(patientRepo.existsById(testPatient.getPatientID()));
    }
}

