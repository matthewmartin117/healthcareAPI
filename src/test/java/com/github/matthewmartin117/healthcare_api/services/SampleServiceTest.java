package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.BiologicalSampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SampleServiceTest {

    @Mock
    private BiologicalSampleRepository sampleRepo;

    @InjectMocks
    private BiologicalSampleService sampleService;

    private Patient testPatient;
    private BiologicalSample testSample;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create Patient
        LocalDate dob = LocalDate.of(1985, 5, 15);

        testPatient = new Patient("P001", "John Doe", dob, new HashMap<>());

    // Create BiologicalSample
    Calendar cal2 = Calendar.getInstance();
    cal2.set(2024, Calendar.OCTOBER, 1);
    Date collectionDate = cal2.getTime();

        testSample = new BiologicalSample("S001", testPatient, "Blood", collectionDate, "Routine check");
    }

    @Test
    void testCreateSample() {
        when(sampleRepo.save(testSample)).thenReturn(testSample);

        BiologicalSample created = sampleService.createSample(testSample);
        assertNotNull(created);
        assertEquals("S001", created.getSampleId());
        verify(sampleRepo, times(1)).save(testSample);
    }

    @Test
    void testGetSampleById() {
        when(sampleRepo.findById("S001")).thenReturn(Optional.of(testSample));

        BiologicalSample sample = sampleService.getSampleById("S001");
        assertNotNull(sample);
        assertEquals("Blood", sample.getSampleType());
    }

    @Test
    void testUpdateSample() {
        BiologicalSample updatedSample = new BiologicalSample("S001", testPatient, "Urine", testSample.getCollectionDate(), "Checkup");

        when(sampleRepo.findById("S001")).thenReturn(Optional.of(testSample));
        when(sampleRepo.save(any(BiologicalSample.class))).thenReturn(updatedSample);

        BiologicalSample result = sampleService.updateSample("S001", updatedSample);
        assertEquals("Urine", result.getSampleType());
        assertEquals("Checkup", result.getReasonCollected());
    }

    @Test
    void testDeleteSample() {
        doNothing().when(sampleRepo).deleteById("S001");

        sampleService.deleteSample("S001");
        verify(sampleRepo, times(1)).deleteById("S001");
    }

    @Test
    void testGetSamplesByPatientId() {
        List<BiologicalSample> sampleList = Arrays.asList(testSample);
        when(sampleRepo.findByPatient_PatientID("P001")).thenReturn(sampleList);

        List<BiologicalSample> result = sampleService.getSamplesByPatientId("P001");
        assertEquals(1, result.size());
        assertEquals("S001", result.get(0).getSampleId());
    }
}
