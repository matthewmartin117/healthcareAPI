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
import com.github.matthewmartin117.healthcare_api.models.AppUser;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.services.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BiologicalSampleRepository sampleRepo;

    private BiologicalSample testSample;
    private Patient testPatient;
    private AppUser testUser;
    private AppUser testAdmin;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setup() {
        patientRepo.deleteAll();
        userRepo.deleteAll();

        // Create test users
        testAdmin = new AppUser();
        testAdmin.setUsername("admin");
        testAdmin.setPassword(passwordEncoder.encode("adminpass"));
        testAdmin.setRoles("ADMIN");
        userRepo.save(testAdmin);

        testUser = new AppUser();
        testUser.setUsername("user");
        testUser.setPassword(passwordEncoder.encode("userpass"));
        testUser.setRoles("USER");
        userRepo.save(testUser);

        // Generate JWT tokens
        adminToken = "Bearer " + jwtService.generateToken(testAdmin.getUsername());
        userToken = "Bearer " + jwtService.generateToken(testUser.getUsername());

        // Create a sample patient
        testPatient = new Patient();
        testPatient.setName("John Doe");
        patientRepo.save(testPatient);

        // create a sample biological sample
        testSample = new BiologicalSample();
        testSample.setPatient(testPatient);
        testSample.setSampleType("Blood");
        testSample.setCollectionDate(new java.util.Date());
        testSample.setReasonCollected("Routine Checkup");
        sampleRepo.save(testSample);

    }

    @Test
    @Order(1)
    void testGetAllSamples() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/biological-samples", testPatient.getPatientID())
        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sampleType", is(testSample.getSampleType())));
    }

    @Test
    @Order(2)
    void testGetSampleById() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/biological-samples/{sampleId}",
                        testPatient.getPatientID(), testSample.getSampleId())
                .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleType", is(testSample.getSampleType())));
    }

    @Test
    @Order(3)
    void testCreateSample() throws Exception {
        BiologicalSample newSample = new BiologicalSample();
        newSample.setPatient(testPatient);
        newSample.setSampleType("Urine");
        newSample.setCollectionDate(new java.util.Date());
        newSample.setReasonCollected("Checkup");

        mockMvc.perform(post("/patients/{patientId}/biological-samples", testPatient.getPatientID())
                        .header("Authorization", userToken)
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
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSample)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleType", is("Tissue")));
    }

    @Test
    @Order(5)
    void testDeleteSample() throws Exception {
        mockMvc.perform(delete("/patients/{patientId}/biological-samples/{sampleId}",
                        testPatient.getPatientID(), testSample.getSampleId())
                        .header("Authorization", adminToken)) // only ADMIN can delete
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testSample.getSampleId().toString())));

        Assertions.assertFalse(sampleRepo.existsById(testSample.getSampleId()));
    }
}
