package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ClinicalNoteService {

  // use dependency injection to supply the clinical note repository
  private final ClinicalNoteRepository noteRepo;

  // use the injected for construction
  public ClinicalNoteService(ClinicalNoteRepository noteRepo){
    this.noteRepo = noteRepo;
  }

  // implement business logic
  // create note
  public ClinicalNote createNote(ClinicalNote note){
    if (note.getPatient().getPatientID() != null ){
    return noteRepo.save(note);
  }
    else {return null;}
  }

  // get all notes for a patient
  public List<ClinicalNote> getNotesByPatientID(String patientId){
    return noteRepo.findByPatient_PatientID(patientId);
  }

  // get a single note by ID
  public ClinicalNote getNoteById(String noteID){
    return noteRepo.findById(noteID).orElse(null);
  }

  // update a note
  public ClinicalNote updateNote(String noteId, ClinicalNote updatedNote){
    return noteRepo.findById(noteId)
        .map(note -> {
            note.setNoteContent(updatedNote.getNoteContent());
            note.setPatient(updatedNote.getPatient());
            return noteRepo.save(note);
        })
        .orElse(null);
  }

  public void deleteNote(String noteId){
    noteRepo.deleteById(noteId);
  }

}
