package com.github.matthewmartin117.healthcare_api.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
public class ClinicalNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClinicalNoteRepository noteRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Patient testPatient;
    private ClinicalNote testNote;
    private AppUser testUser;
    private AppUser testAdmin;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setup() {
        noteRepo.deleteAll();
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

        // Generate tokens
        adminToken = "Bearer " + jwtService.generateToken(testAdmin.getUsername());
        userToken = "Bearer " + jwtService.generateToken(testUser.getUsername());

        // Create patient and clinical note
        testPatient = new Patient();
        testPatient.setName("John Doe");
        patientRepo.save(testPatient);

        testNote = new ClinicalNote();
        testNote.setPatient(testPatient);
        testNote.setNoteContent("Initial note content");
        noteRepo.save(testNote);
    }

    @Test
    @Order(1)
    void testGetAllClinicalNotes() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/clinical-notes", testPatient.getPatientID())
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].noteContent", is(testNote.getNoteContent())));
    }

    @Test
    @Order(2)
    void testGetClinicalNoteById() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/clinical-notes/{noteId}",
                        testPatient.getPatientID(), testNote.getNoteId())
                        .header("Authorization", userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteContent", is(testNote.getNoteContent())));
    }

    @Test
    @Order(3)
    void testCreateClinicalNote() throws Exception {
        ClinicalNote newNote = new ClinicalNote();
        newNote.setNoteContent("New note content");
        newNote.setPatient(testPatient);

        mockMvc.perform(post("/patients/{patientId}/clinical-notes", testPatient.getPatientID())
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteContent", is("New note content")));

        Assertions.assertEquals(2, noteRepo.findAll().size());
    }

    @Test
    @Order(4)
    void testUpdateClinicalNote() throws Exception {
        testNote.setNoteContent("Updated note");
        mockMvc.perform(put("/patients/{patientId}/clinical-notes/{noteId}",
                        testPatient.getPatientID(), testNote.getNoteId())
                        .header("Authorization", adminToken) // only ADMIN can update
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteContent", is("Updated note")));
    }

    @Test
    @Order(5)
    void testDeleteClinicalNote() throws Exception {
        mockMvc.perform(delete("/patients/{patientId}/clinical-notes/{noteId}",
                        testPatient.getPatientID(), testNote.getNoteId())
                        .header("Authorization", adminToken)) // only ADMIN can delete
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testNote.getNoteId().toString())));

        Assertions.assertFalse(noteRepo.existsById(testNote.getNoteId()));
    }
}
