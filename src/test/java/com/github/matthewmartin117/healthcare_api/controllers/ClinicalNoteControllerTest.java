package com.github.matthewmartin117.healthcare_api.controllers;

import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
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
public class ClinicalNoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClinicalNoteRepository noteRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Patient testPatient;
    private ClinicalNote testNote;

    @BeforeEach
    void setup() {
        noteRepo.deleteAll();
        patientRepo.deleteAll();

    testPatient = new Patient("P001", "John Doe", java.time.LocalDate.now(), new HashMap<>());
        patientRepo.save(testPatient);

        testNote = new ClinicalNote();
        testNote.setNoteContent("Initial note content");
        testNote.setPatient(testPatient);
        noteRepo.save(testNote);
    }

    @Test
    @Order(1)
    void testGetAllClinicalNotes() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/clinical-notes", testPatient.getPatientID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].noteContent", is(testNote.getNoteContent())));
    }

    @Test
    @Order(2)
    void testGetClinicalNoteById() throws Exception {
        mockMvc.perform(get("/patients/{patientId}/clinical-notes/{noteId}",
                        testPatient.getPatientID(), testNote.getNoteId()))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testNote)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.noteContent", is("Updated note")));
    }

    @Test
    @Order(5)
    void testDeleteClinicalNote() throws Exception {
        mockMvc.perform(delete("/patients/{patientId}/clinical-notes/{noteId}",
                        testPatient.getPatientID(), testNote.getNoteId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(testNote.getNoteId())));

        Assertions.assertFalse(noteRepo.existsById(testNote.getNoteId()));
    }
}
