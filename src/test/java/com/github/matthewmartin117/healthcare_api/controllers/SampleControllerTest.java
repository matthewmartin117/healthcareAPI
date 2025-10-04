package com.github.matthewmartin117.healthcare_api.controllers;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.BiologicalSampleRepository;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BiologicalSampleRepository sampleRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient testPatient;
    private BiologicalSample testSample;

    @BeforeEach
    void setup() {
        sampleRepo.deleteAll();
        patientRepo.deleteAll();

    testPatient = new Patient("P001", "John Doe", LocalDate.now(), new HashMap<>());
        patientRepo.save(testPatient);

    testSample = new BiologicalSample("S001", testPatient, "Blood", new java.util.Date(), "Routine check");
        sampleRepo.save(testSample);
    }

    @Test
    @Order(1)
    void testGetAllSamples() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/biological-samples", testPatient.getPatientID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sampleType", is(testSample.getSampleType())));
    }

    @Test
    @Order(2)
    void testGetSampleById() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/biological-samples/{sampleId}",
                        testPatient.getPatientID(), testSample.getSampleId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleType", is(testSample.getSampleType())));
    }

    @Test
    @Order(3)
    void testCreateSample() throws Exception {
    BiologicalSample newSample = new BiologicalSample("S002", testPatient, "Urine", new java.util.Date(), "Checkup");
        mockMvc.perform(post("/patients/{patientId}/biological-samples", testPatient.getPatientID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleType", is("Urine")));

        Assertions.assertEquals(2, sampleRepo.findAll().size());
    }

    @Test
    @Order(4)
    void testUpdateSample() throws Exception {
        testSample.setSampleType("Tissue");
        mockMvc.perform(put("/patients/{patientId}/biological-samples/{sampleId}",
                        testPatient.getPatientID(), testSample.getSampleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleType", is("Tissue")));
    }

    @Test
    @Order(5)
    void testDeleteSample() throws Exception {
        mockMvc.perform(delete("/patients/{patientId}/biological-samples/{sampleId}",
                        testPatient.getPatientID(), testSample.getSampleId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testSample.getSampleId())));

        Assertions.assertFalse(sampleRepo.existsById(testSample.getSampleId()));
    }
}
