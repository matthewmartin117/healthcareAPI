package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClinicalNoteServiceTest {

    @Mock
    private ClinicalNoteRepository noteRepo;

    @InjectMocks
    private ClinicalNoteService noteService;

    private Patient testPatient;
    private ClinicalNote testNote;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testPatient = new Patient();
        testPatient.setPatientID(1L);

        testNote = new ClinicalNote();
        testNote.setNoteId(100L);
        testNote.setPatient(testPatient);
        testNote.setNoteContent("Initial content");
    }

    @Test
    void testCreateNote() {
        when(noteRepo.save(any(ClinicalNote.class))).thenReturn(testNote);
        ClinicalNote saved = noteService.createNote(testNote);
        assertEquals("Initial content", saved.getNoteContent());
        verify(noteRepo, times(1)).save(any(ClinicalNote.class));
    }

    @Test
    void testGetNoteById() {
        when(noteRepo.findById(100L)).thenReturn(Optional.of(testNote));
        ClinicalNote found = noteService.getNoteById(100L);
        assertEquals(100L, found.getNoteId());
        verify(noteRepo, times(1)).findById(100L);
    }

    @Test
    void testGetNotesForPatient() {
        when(noteRepo.findByPatient_PatientID(testPatient.getPatientID()))
                .thenReturn(Collections.singletonList(testNote));

        List<ClinicalNote> notes = noteService.getNotesByPatientID(testPatient.getPatientID());

        assertEquals(1, notes.size());
        assertEquals(testNote.getNoteContent(), notes.get(0).getNoteContent());
        verify(noteRepo, times(1)).findByPatient_PatientID(testPatient.getPatientID());
    }

    @Test
    void testUpdateNote() {
        when(noteRepo.findById(testNote.getNoteId())).thenReturn(Optional.of(testNote));
        when(noteRepo.save(any(ClinicalNote.class))).thenReturn(testNote);

        testNote.setNoteContent("Updated content");
        ClinicalNote updated = noteService.updateNote(testNote.getNoteId(), testNote);

        assertNotNull(updated);
        assertEquals("Updated content", updated.getNoteContent());
        verify(noteRepo, times(1)).save(any(ClinicalNote.class));
    }

    @Test
    void testDeleteNote() {
        doNothing().when(noteRepo).deleteById(testNote.getNoteId());

        noteService.deleteNote(testNote.getNoteId());

        verify(noteRepo, times(1)).deleteById(testNote.getNoteId());
    }
}
