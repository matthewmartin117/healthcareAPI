package com.github.matthewmartin117.healthcare_api.models;
import java.time.LocalDate;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
/* This class represents a rrecord of a specific patient
 * Essential indentifiers and demographic information
 * Contact information
 * A record of clinical notes about the patient
 * Any biological samples taken from the patient
 */
@Entity // marks this as a JPA entity to be mapped to a database table
@Table(name = "patient") // specifies the table name in the database , otherwise defaults to class name
public class Patient {
// properties
  // Essential identifiers
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long patientID; // Unique patient identifier

  private String name;
  private LocalDate dateOfBirth;

  @ElementCollection
  @CollectionTable(name = "contact_information", joinColumns = @JoinColumn(name = "patient_patientid"))
  @MapKeyColumn(name = "contact_type") // e.g. phone, email
  @Column(name = "contact_val") // e.g. actual phone number or email address
  private Map<String, String> contactInformation = new HashMap<>();

  // Associated Patient Records
  @OneToMany(mappedBy = "patient",cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<ClinicalNote> clinicalNotes; // List of clinical notes
  @OneToMany(mappedBy = "patient",cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List <BiologicalSample> biologicalSamples; // List of biological samples

  // Constructor
  public Patient(String name, LocalDate dateOfBirth, Map<String, String> contactInformation) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.contactInformation = contactInformation;
    this.clinicalNotes = new ArrayList<>();
    this.biologicalSamples = new ArrayList<>();
  }
// no argument contructor
  public Patient() {
    // No-argument constructor for testing and JPA
    // Initialize collections to avoid null-related NPEs when JPA constructs entities
    this.clinicalNotes = new ArrayList<>();
    this.biologicalSamples = new ArrayList<>();
    }

  // getters and setters
  public Long getPatientID() {
    return patientID;
}
  public void setPatientID(Long patientID){
    this.patientID = patientID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name){
    this.name = name;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate Dob){
    this.dateOfBirth = Dob;
  }

  public Map<String, String> getContactInformation() {
    return contactInformation;
  }

  public void setContactInformation(Map<String, String> contactInformation) {
    this.contactInformation = contactInformation;
  }

  public List<ClinicalNote> getClinicalNotes() {
    return clinicalNotes == null ? Collections.emptyList() : Collections.unmodifiableList(clinicalNotes);
}

  public void addClinicalNote(ClinicalNote note) {
    this.clinicalNotes.add(note);
  }

  public List<BiologicalSample> getBiologicalSamples() {
    return biologicalSamples == null ? Collections.emptyList() : Collections.unmodifiableList(biologicalSamples);
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