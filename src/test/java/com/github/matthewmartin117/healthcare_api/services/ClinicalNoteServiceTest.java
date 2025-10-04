package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClinicalNoteServiceTest {

    @Mock
    private ClinicalNoteRepository noteRepo;

    @InjectMocks
    private ClinicalNoteService noteService;

    private ClinicalNote testNote;
    private Patient testPatient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

    testPatient = new Patient("P001", "John Doe", java.time.LocalDate.now(), new HashMap<>());
        testNote = new ClinicalNote();
        testNote.setNoteId("N001");
        testNote.setNoteContent("Initial note content");
        testNote.setPatient(testPatient);
    }

    @Test
    void testCreateNote() {
        when(noteRepo.save(any(ClinicalNote.class))).thenReturn(testNote);

        ClinicalNote created = noteService.createNote(testNote);
        assertNotNull(created);
        assertEquals("Initial note content", created.getNoteContent());

        verify(noteRepo, times(1)).save(testNote);
    }

    @Test
    void testGetNotesByPatientID() {
        when(noteRepo.findByPatient_PatientID("P001")).thenReturn(List.of(testNote));

        List<ClinicalNote> notes = noteService.getNotesByPatientID("P001");
        assertEquals(1, notes.size());
        assertEquals("Initial note content", notes.get(0).getNoteContent());

        verify(noteRepo, times(1)).findByPatient_PatientID("P001");
    }

    @Test
    void testGetNoteById() {
        when(noteRepo.findById("N001")).thenReturn(Optional.of(testNote));

        ClinicalNote found = noteService.getNoteById("N001");
        assertNotNull(found);
        assertEquals("Initial note content", found.getNoteContent());

        verify(noteRepo, times(1)).findById("N001");
    }

    @Test
    void testUpdateNote() {
        ClinicalNote updatedNote = new ClinicalNote();
        updatedNote.setNoteContent("Updated content");
        updatedNote.setPatient(testPatient);

        when(noteRepo.findById("N001")).thenReturn(Optional.of(testNote));
        when(noteRepo.save(any(ClinicalNote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClinicalNote result = noteService.updateNote("N001", updatedNote);
        assertEquals("Updated content", result.getNoteContent());

        verify(noteRepo, times(1)).findById("N001");
        verify(noteRepo, times(1)).save(testNote);
    }

    @Test
    void testDeleteNote() {
        doNothing().when(noteRepo).deleteById("N001");

        noteService.deleteNote("N001");

        verify(noteRepo, times(1)).deleteById("N001");
    }
}
