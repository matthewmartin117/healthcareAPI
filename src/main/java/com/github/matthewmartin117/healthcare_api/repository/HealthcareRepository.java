package com.github.matthewmartin117.healthcare_api.repository;
import java.util.*;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;

// annotated as a service for Spring to manage
// indicates that this bean provides business functionalities
// spring automattically manages its lifecycle and allows it to be injected into other components
import org.springframework.stereotype.Service;
@Service
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
    public Patient createPatient(Patient patient) {
      patientStore.put(patient.getPatientID(), patient);
      return patient;
    }

    // Get patient by ID: looks up a patient ID and returns the corresponding patient object
    public Patient getPatientByID(String patientID) {
      return patientStore.get(patientID);
    }

    // Update patient: replaces the existing patient record with an updated Patient object
    public Patient updatePatient(String patientID, Patient updatedPatient) {
      patientStore.put(patientID, updatedPatient);
      return updatedPatient;
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
    public BiologicalSample createBiologicalSample(BiologicalSample sample) {
      String patientID = sample.getPatientID();
      biologicalSampleStore
            .computeIfAbsent(patientID, k -> new ArrayList<>())
            .add(sample);
            return sample;
    }

    // Get biological samples by patient ID: returns the list of samples for a patient
    public List<BiologicalSample> getBiologicalSamplesByPatientID(String patientID) {
      return biologicalSampleStore.getOrDefault(patientID, Collections.emptyList());
    }

    // Get a specific biological sample by its ID for a patient
    public BiologicalSample getBiologicalSampleByID(String patientID, String sampleID) {
      List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
      if (samples != null) {
          for (BiologicalSample sample : samples) {
              if (sample.getId().equals(sampleID)) {
                  return sample;
              }
          }
      }
      return null; // Sample not found
    }

    // Update biological sample: replaces the sample at the specified index
    public BiologicalSample updateBiologicalSample(String patientID, String sampleID, BiologicalSample updatedSample){
      List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
      if (samples != null) {
          for (int i = 0; i < samples.size(); i++) {
              if (samples.get(i).getId().equals(sampleID)) {
                  samples.set(i, updatedSample);
                }
          }
      }
      return updatedSample;
    }


    // Delete biological sample: removes a sample at the specified index
    public void deleteBiologicalSample(String patientID, String sampleID){
      List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
      if (samples != null) {
          samples.removeIf(s -> s.getId().equals(sampleID));
      }
    }

    /* ============================
        CLINICAL NOTE CRUD
    ============================ */

    // Create clinical note: adds a ClinicalNote to the store using patientID
    public ClinicalNote createClinicalNote(ClinicalNote note) {
      String patientID = note.getPatientID();
      clinicalNoteStore
            .computeIfAbsent(patientID, k -> new ArrayList<>())
            .add(note);
            return note;
    }

    // Get clinical notes by patient ID: returns the list of notes for a patient
    public List<ClinicalNote> getClinicalNotesByPatientID(String patientID) {
      return clinicalNoteStore.getOrDefault(patientID, Collections.emptyList());
    }

    // Get a specific clinical note by its ID for a patient
    public ClinicalNote getClinicalNoteByID(String patientID, String noteID) {
      List<ClinicalNote> notes = clinicalNoteStore.get(patientID);
      if (notes != null) {
          for (ClinicalNote note : notes) {
              if (note.getId().equals(noteID)) {
                  return note;
              }
          }
      }
      return null; // Note not found
    }

    // Update clinical note: replaces the note at the specified index
    public ClinicalNote updateClinicalNote(String patientID, String noteID, ClinicalNote updatedNote){
      List<ClinicalNote> notes = clinicalNoteStore.get(patientID);
      if (notes != null) {
          for (int i = 0; i < notes.size(); i++) {
              if (notes.get(i).getId().equals(noteID)) {
                  notes.set(i, updatedNote);
              }
          }
      }
      return updatedNote;
    }

    // Delete clinical note: removes a note at the specified ID
    public void deleteClinicalNote(String patientID, String noteID){
    List<ClinicalNote> notes = clinicalNoteStore.get(patientID);
    if (notes != null) {
        notes.removeIf(n -> n.getId().equals(noteID));
    }
  }
}
