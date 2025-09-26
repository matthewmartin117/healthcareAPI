package com.github.matthewmartin117.healthcare_api;
import java.util.Map;
// this is a simple class the stores patients in-memory
public class PatientRepository {
  // This stores patients according to their patientID
  private Map< String, Patient> patientStore;


  // Constructor
  public PatientRepository(Map<String, Patient> patientStore) {
    this.patientStore = patientStore;
  }

// These are associated methods to add, retrieve, and update patients

// Create
// CreatePatient takes a Patient object and adds it to the patientStore
    public void createPatient(Patient patient){
    // take a patient object and add it to the patientStore
    patientStore.put(patient.getPatientID(), patient);
  }
// Read
// getpatientByID looks. up a patient ID and returns the corresponding patient object
  public Patient getPatientByID(String patientID){
    return patientStore.get(patientID);
  }

// Update
// updatePatientContactInformation takes a patient ID and an updated Paitent object
// it updates the existing patient record with the new patient information
  public void updatePatient(String patientID,Patient updatedPatient){
    patientStore.put(patientID, updatedPatient);
    return;
  }

// Delete
// DeletePatient removes a patient from the patientStore based on their patientID
  public void deletePatient(String patientID){
    patientStore.remove(patientID);
    return;
  }

// Get all patients returns the entire patientStore
  public Map<String, Patient> getAllPatients(){
    return patientStore;
  }

}