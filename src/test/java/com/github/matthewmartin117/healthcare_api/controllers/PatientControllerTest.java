package com.github.matthewmartin117.healthcare_api.controllers;

import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient testPatient;

    @BeforeEach
    void setup() {
    patientRepo.deleteAll(); // Clean DB before each test
    testPatient = new Patient("P001", "John Doe", java.time.LocalDate.now(), new HashMap<>());
        patientRepo.save(testPatient);
    }

    @Test
    @Order(1)
    void testGetAllPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientID", is(testPatient.getPatientID())));
    }

    @Test
    @Order(2)
    void testGetPatientByID() throws Exception {
        mockMvc.perform(get("/patients/{id}", testPatient.getPatientID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientID", is(testPatient.getPatientID())))
                .andExpect(jsonPath("$.name", is(testPatient.getName())));
    }

    @Test
    @Order(3)
    void testCreatePatient() throws Exception {
    Patient newPatient = new Patient("P002", "Jane Smith", java.time.LocalDate.now(), new HashMap<>());
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientID", is("P002")));

        Assertions.assertEquals(2, patientRepo.findAll().size()); // verify DB persisted
    }

    @Test
    @Order(4)
    void testUpdatePatient() throws Exception {
        testPatient.setName("Updated Name");
        mockMvc.perform(put("/patients/{id}", testPatient.getPatientID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));

        Assertions.assertEquals("Updated Name", patientRepo.findById(testPatient.getPatientID()).get().getName());
    }

    @Test
    @Order(5)
    void testDeletePatient() throws Exception {
        mockMvc.perform(delete("/patients/{id}", testPatient.getPatientID()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testPatient.getPatientID())));

        Assertions.assertFalse(patientRepo.existsById(testPatient.getPatientID()));
    }

}
