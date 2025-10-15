package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.BiologicalSampleRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SampleServiceTest {

    @Mock
    private BiologicalSampleRepository sampleRepo;

    @InjectMocks
    private BiologicalSampleService sampleService;

    private Patient testPatient;
    private BiologicalSample testSample;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        testPatient = new Patient();
        testPatient.setPatientID(1L);

        testSample = new BiologicalSample();
        testSample.setSampleId(200L);
        testSample.setPatient(testPatient);
        testSample.setSampleType("Blood");
    }

    @Test
    void testCreateSample() {
        when(sampleRepo.save(any(BiologicalSample.class))).thenReturn(testSample);

        BiologicalSample saved = sampleService.createSample(testSample);

        assertEquals("Blood", saved.getSampleType());
        verify(sampleRepo, times(1)).save(any(BiologicalSample.class));
    }

    @Test
    void testGetSampleById() {
        when(sampleRepo.findById(200L)).thenReturn(Optional.of(testSample));

        BiologicalSample found = sampleService.getSampleById(200L);

        assertEquals(200L, found.getSampleId());
        verify(sampleRepo, times(1)).findById(200L);
    }

    @Test
    void testGetSamplesForPatient() {
        when(sampleRepo.findByPatient_PatientID(testPatient.getPatientID()))
                .thenReturn(Collections.singletonList(testSample));

        List<BiologicalSample> samples = sampleService.getSamplesByPatientId(testPatient.getPatientID());

        assertEquals(1, samples.size());
        assertEquals("Blood", samples.get(0).getSampleType());
        verify(sampleRepo, times(1)).findByPatient_PatientID(testPatient.getPatientID());
    }

    @Test
    void testUpdateSample() {
        when(sampleRepo.findById(testSample.getSampleId())).thenReturn(Optional.of(testSample));
        when(sampleRepo.save(any(BiologicalSample.class))).thenReturn(testSample);

        testSample.setSampleType("Urine");
        BiologicalSample updated = sampleService.updateSample(testSample.getSampleId(), testSample);

        assertNotNull(updated);
        assertEquals("Urine", updated.getSampleType());
        verify(sampleRepo, times(1)).findById(testSample.getSampleId());
        verify(sampleRepo, times(1)).save(any(BiologicalSample.class));
    }

    @Test
    void testDeleteSample() {
        doNothing().when(sampleRepo).deleteById(testSample.getSampleId());

        sampleService.deleteSample(testSample.getSampleId());

        verify(sampleRepo, times(1)).deleteById(testSample.getSampleId());
    }
}
