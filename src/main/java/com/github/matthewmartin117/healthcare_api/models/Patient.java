package com.github.matthewmartin117.healthcare_api.models;
import java.util.*;
/* This class represents a rrecord of a specific patient
 * Essential indentifiers and demographic information
 * Contact information
 * A record of clinical notes about the patient
 * Any biological samples taken from the patient
 */
public class Patient {
// properties
  // Essential identifiers
  private String patientID;
  private String name;
  private Date dateOfBirth;
  private Map<String, String> contactInformation; // e.g. phone number, email, address
  // Associated Patient Records
  private List<ClinicalNote> clinicalNotes; // List of clinical notes
  private List <BiologicalSample> biologicalSamples; // List of biological samples

  // Constructor
  public Patient(String patientID, String name, Date dateOfBirth, Map<String, String> contactInformation) {
    this.patientID = patientID;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.contactInformation = contactInformation;
    this.clinicalNotes = new ArrayList<>();
    this.biologicalSamples = new ArrayList<>();
  }

  // getters and setters
  public String getPatientID() {
    return patientID;
}

  public String getName() {
    return name;
  }


  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public Map<String, String> getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(Map<String, String> contactInformation) {
    this.contactInformation = contactInformation;
  }

  public List<ClinicalNote> getClinicalNotes() {
    return Collections.unmodifiableList(clinicalNotes);
}

  public void addClinicalNote(ClinicalNote note) {
    this.clinicalNotes.add(note);
  }

  public List<BiologicalSample> getBiologicalSamples() {
    return Collections.unmodifiableList(biologicalSamples);
  }

  public void addBiologicalSample(BiologicalSample sample) {
    this.biologicalSamples.add(sample);
  }

  @Override
public String toString() {
    return "Patient{" +
            "id='" + patientID + '\'' +
            ", name='" + name + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", contactInformation=" + contactInformation +
            ", clinicalNotes=" + clinicalNotes +
            ", biologicalSamples=" + biologicalSamples +
            '}';
}

}