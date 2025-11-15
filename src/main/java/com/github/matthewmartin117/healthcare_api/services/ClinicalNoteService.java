package com.github.matthewmartin117.healthcare_api.services;

import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.matthewmartin117.healthcare_api.repositories.ClinicalNoteRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ClinicalNoteService {

  private static final Logger log = LoggerFactory.getLogger(ClinicalNoteService.class);

  // use dependency injection to supply the clinical note repository
  private final ClinicalNoteRepository noteRepo;
  private final PHIRedactionService redactionService;

  // use the injected for construction
  public ClinicalNoteService(ClinicalNoteRepository noteRepo, PHIRedactionService redactionService){
    this.noteRepo = noteRepo;
    this.redactionService = redactionService;
  }

  // implement business logic
  // create note
  // implement PHI redaction before saving
  public ClinicalNote createNote(ClinicalNote note){
    if (note.getPatient().getPatientID() != null ){
      log.debug("Creating clinical note for patientId={} with original content length={}",
                note.getPatient().getPatientID(),
                note.getNoteContent() == null ? 0 : note.getNoteContent().length());

      String redactedContent = redactionService.redact(note.getNoteContent());
      note.setNoteContent(redactedContent);
      log.debug("Saving clinical note for patientId={} with redacted content length={}",
                note.getPatient().getPatientID(),
                redactedContent == null ? 0 : redactedContent.length());
      return noteRepo.save(note);
    } else {
      throw new IllegalArgumentException("Patient ID cannot be null");
    }
  }

  // get all notes for a patient
  public List<ClinicalNote> getNotesByPatientID(Long patientId){
    return noteRepo.findByPatient_PatientID(patientId);
  }

  // get a single note by ID
  public ClinicalNote getNoteById(Long noteID){
    return noteRepo.findById(noteID).orElse(null);
  }

  // update a note
  public ClinicalNote updateNote(Long noteId, ClinicalNote updatedNote){
    return noteRepo.findById(noteId)
        .map(note -> {
            note.setNoteContent(updatedNote.getNoteContent());
            note.setPatient(updatedNote.getPatient());
            return noteRepo.save(note);
        })
        .orElse(null);
  }

  public void deleteNote(Long noteId){
    noteRepo.deleteById(noteId);
  }

}
