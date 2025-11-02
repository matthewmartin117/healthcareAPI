package com.github.matthewmartin117.healthcare_api.controllers;

import com.github.matthewmartin117.healthcare_api.models.AppUser;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="admin", roles={"ADMIN"})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockBean
    private com.github.matthewmartin117.healthcare_api.services.PHIRedactionService phiRedactionService;

    private String adminToken;
    private String userToken;
    private AppUser testAdmin;
    private AppUser testUser;
    private Patient savedPatient; // will hold a real generated patient


    @BeforeEach
    void setup() {
    userRepo.deleteAll();
    testAdmin = new AppUser();
    testAdmin.setUsername("admin");
    testAdmin.setPassword(passwordEncoder.encode("adminpassword")); // must encode
    testAdmin.setRoles("ADMIN");
    userRepo.save(testAdmin);

    testUser = new AppUser();
    testUser.setUsername("user");
    testUser.setPassword(passwordEncoder.encode("userpassword"));
    testUser.setRoles("USER");
    userRepo.save(testUser);

    adminToken = "Bearer " + jwtService.generateToken(testAdmin.getUsername());
    userToken = "Bearer " + jwtService.generateToken(testUser.getUsername());
        // Create a sample patient with a generated ID
        Patient testpatient = new Patient();
        testpatient.setName("John Doe");
        savedPatient = patientRepo.save(testpatient);
        // make PHI redactor return the original text during tests to avoid external HTTP calls
        when(phiRedactionService.redact(anyString())).thenAnswer(inv -> inv.getArgument(0));
    }

    // ------------------ GET ENDPOINTS ------------------

    @Test
    void adminCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(post("/patients")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane Doe\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void userCanAccessGetPatients() throws Exception {
        mockMvc.perform(get("/patients")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void anonymousCannotAccessPatients() throws Exception {
        mockMvc.perform(get("/patients")
                        .header("Authorization", "Invalid token"))
                .andExpect(status().isUnauthorized());
    }

    // ------------------ PUT / DELETE / POST ------------------

    @Test
    void userCannotDeletePatient() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/" + savedPatient.getPatientID())
                .header("Authorization", userToken))
                .andExpect(status().isForbidden()); // USER can't delete
    }

    @Test
    void adminCanDeletePatient() throws Exception {
        mockMvc.perform(delete("/patients/" + savedPatient.getPatientID())
                        .header("Authorization", adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanUpdatePatient() throws Exception {
        mockMvc.perform(put("/patients/" + savedPatient.getPatientID())
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanCreatePatient() throws Exception {
        mockMvc.perform(post("/patients")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Patient\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void invalidTokenCannotAccessProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/patients")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized());
    }

    // ------------------ CLINICAL NOTES ------------------

    @Test
    void userCanCreateClinicalNote() throws Exception {
        mockMvc.perform(post("/patients/" + savedPatient.getPatientID() + "/clinical-notes")
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"noteContent\":\"Patient is recovering well.\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void userCanGetClinicalNotes() throws Exception {
        mockMvc.perform(get("/patients/" + savedPatient.getPatientID() + "/clinical-notes")
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void adminCanCreateClinicalNote() throws Exception {
        mockMvc.perform(post("/patients/" + savedPatient.getPatientID() + "/clinical-notes")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"noteContent\":\"Admin note.\"}"))
                .andExpect(status().isOk());
    }
}
