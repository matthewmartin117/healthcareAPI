package com.github.matthewmartin117.healthcare_api.controllers;

import com.github.matthewmartin117.healthcare_api.models.AppUser;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    @Order(1)
    void testGetAllPatients() throws Exception {
        mockMvc.perform(get("/patients")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patientID", is(testPatient.getPatientID().intValue())));
    }

    @Test
    @Order(2)
    void testGetPatientByID() throws Exception {
        mockMvc.perform(get("/patients/{id}", testPatient.getPatientID())
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientID", is(testPatient.getPatientID().intValue())))
                .andExpect(jsonPath("$.name", is(testPatient.getName())));
    }

    @Test
    @Order(3)
    void testCreatePatient() throws Exception {
        Patient newPatient = new Patient("Jane Smith", java.time.LocalDate.now(), new java.util.HashMap<>());

        mockMvc.perform(post("/patients")
                        .header("Authorization", userToken) // USER can create
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isOk());

        Assertions.assertEquals(2, patientRepo.findAll().size());
    }

    @Test
    @Order(4)
    void testUpdatePatient() throws Exception {
        testPatient.setName("Updated Name");

        mockMvc.perform(put("/patients/{id}", testPatient.getPatientID())
                        .header("Authorization", adminToken) // only ADMIN can update
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")));

        Assertions.assertEquals("Updated Name",
                patientRepo.findById(testPatient.getPatientID()).get().getName());
    }

    @Test
    @Order(5)
    void testDeletePatient() throws Exception {
        mockMvc.perform(delete("/patients/{id}", testPatient.getPatientID())
                        .header("Authorization", adminToken)) // only ADMIN can delete
                .andExpect(status().isOk());

        Assertions.assertFalse(patientRepo.existsById(testPatient.getPatientID()));
    }
}
