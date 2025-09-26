package com.github.matthewmartin117.healthcare_api;

import java.util.*;

public class HealthcareRepository {

  // Stores patients according to their patientID
  private Map<String, Patient> patientStore;

  // Stores clinical notes by patientID
  private Map<String, List<ClinicalNote>> clinicalNoteStore;

  // Stores biological samples by patientID
  private Map<String, List<BiologicalSample>> biologicalSampleStore;

  // Constructor: initialize all stores as empty hashmaps
  public HealthcareRepository() {
    this.patientStore = new HashMap<>();
    this.clinicalNoteStore = new HashMap<>();
    this.biologicalSampleStore = new HashMap<>();
  }

  /* ============================
       PATIENT CRUD
  ============================ */

  // Create patient: takes a Patient object and adds it to the patientStore
  public void createPatient(Patient patient) {
    patientStore.put(patient.getPatientID(), patient);
  }

  // Get patient by ID: looks up a patient ID and returns the corresponding patient object
  public Patient getPatientByID(String patientID) {
    return patientStore.get(patientID);
  }

  // Update patient: replaces the existing patient record with an updated Patient object
  public void updatePatient(String patientID, Patient updatedPatient) {
    patientStore.put(patientID, updatedPatient);
  }

  // Delete patient: removes a patient and associated data from all stores
  public void deletePatient(String patientID) {
    patientStore.remove(patientID);
    biologicalSampleStore.remove(patientID); // cleanup related data
    clinicalNoteStore.remove(patientID);
  }

  // Get all patients: returns the entire patientStore
  public Map<String, Patient> getAllPatients() {
    return patientStore;
  }

  /* ============================
       BIOLOGICAL SAMPLE CRUD
  ============================ */

  // Create biological sample: adds a BiologicalSample to the store using patientID
  public void createBiologicalSample(BiologicalSample sample) {
    String patientID = sample.getPatientID();
    biologicalSampleStore
          .computeIfAbsent(patientID, k -> new ArrayList<>())
          .add(sample);
  }

  // Get biological samples by patient ID: returns the list of samples for a patient
  public List<BiologicalSample> getBiologicalSamplesByPatientID(String patientID) {
    return biologicalSampleStore.getOrDefault(patientID, Collections.emptyList());
  }

  // Update biological sample: replaces the sample at the specified index
  public void updateBiologicalSample(String patientID, int sampleIndex, BiologicalSample updatedSample) {
    List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
    if (samples != null && sampleIndex >= 0 && sampleIndex < samples.size()) {
      samples.set(sampleIndex, updatedSample);
    }
  }

  // Delete biological sample: removes a sample at the specified index
  public void deleteBiologicalSample(String patientID, int sampleIndex) {
    List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
    if (samples != null && sampleIndex >= 0 && sampleIndex < samples.size()) {
      samples.remove(sampleIndex);
    }
  }

  /* ============================
       CLINICAL NOTE CRUD
  ============================ */

  // Create clinical note: adds a ClinicalNote to the store using patientID
  public void createClinicalNote(ClinicalNote note) {
    String patientID = note.getPatientID();
    clinicalNoteStore
          .computeIfAbsent(patientID, k -> new ArrayList<>())
          .add(note);
  }

  // Get clinical notes by patient ID: returns the list of notes for a patient
  public List<ClinicalNote> getClinicalNotesByPatientID(String patientID) {
    return clinicalNoteStore.getOrDefault(patientID, Collections.emptyList());
  }

  // Update clinical note: replaces the note at the specified index
  public void updateClinicalNote(String patientID, int noteIndex, ClinicalNote updatedNote) {
    List<ClinicalNote> notes = clinicalNoteStore.get(patientID);
    if (notes != null && noteIndex >= 0 && noteIndex < notes.size()) {
      notes.set(noteIndex, updatedNote);
    }
  }

  // Delete clinical note: removes a note at the specified index
  public void deleteClinicalNote(String patientID, int noteIndex) {
    List<ClinicalNote> notes = clinicalNoteStore.get(patientID);
    if (notes != null && noteIndex >= 0 && noteIndex < notes.size()) {
      notes.remove(noteIndex);
    }
  }
}
