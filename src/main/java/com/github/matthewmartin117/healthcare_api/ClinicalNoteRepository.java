package com.github.matthewmartin117.healthcare_api;

import java.util.List;

// this is a simple class the stores Clinical notes for each patient in-memory
  // It stores a list of Clinical notes according to their patientID
  // each patientID can have multiple clinical
public class ClinicalNoteRepository {
   private java.util.Map< String, List <ClinicalNote> > ClinicalNoteStore;

  // constructor
  public ClinicalNoteRepository(java.util.Map< String, List <ClinicalNote> > ClinicalNoteStore) {
    this.ClinicalNoteStore = ClinicalNoteStore;
  }

  // Create
  // createClinicalNote takes a ClinicalNote object and adds it to the ClinicalNoteStore
  public void createClinicalNote(ClinicalNote note){
    // take a clincial note and add it to the clinicalnotestore, using patientID
    String patientID = note.getPatientID();
     // if the patient ID exists in the store, add the note to the list
    if (ClinicalNoteStore.containsKey(patientID)){
      ClinicalNoteStore.get(patientID).add(note);
    } else {
      // if the patient ID does not exist, create a new list and add the note
      List<ClinicalNote> newNoteList = new java.util.ArrayList<>();
      newNoteList.add(note);
      ClinicalNoteStore.put(patientID, newNoteList);
     }
  }
  // Read
  // getClinicalNotesByPatientID looks up a patient ID and returns the corresponding list of clinical notes
  public List<ClinicalNote> getClinicalNotesByPatientID(String patientID){
    return ClinicalNoteStore.get(patientID);
  }
  // Update
  // updateClinicalNote takes a patient ID, an index of the note in the list, and an updated ClinicalNote object
  // it updates the existing clinical note record with the new clinical note information
  public void updateClinicalNote(String patientID, int noteIndex, ClinicalNote updatedNote){
    if (ClinicalNoteStore.containsKey(patientID)){
      // retrive the list of notes for the patient
      List<ClinicalNote> notes = ClinicalNoteStore.get(patientID);
      // if the note index is valid, update the note at that index
      if (noteIndex >= 0 && noteIndex < notes.size()){
        // set the note at the specified index to the updated note
        notes.set(noteIndex, updatedNote);
      }
    }
  }
  // Delete Clinical Note removes a clinical note from the clinical note store based on their patientID and note index
  public void deleteClinicalNote(String patientID, int noteIndex){
    if(ClinicalNoteStore.containsKey(patientID)){
      // if the index is valid
      if(noteIndex >= 0 && noteIndex < ClinicalNoteStore.get(patientID).size()){
        // remove the note at the specified index
        ClinicalNoteStore.get(patientID).remove(noteIndex);
      }
    }
  }

}
