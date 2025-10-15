package com.github.matthewmartin117.healthcare_api.models;
/* This class represents a clinical note about a patient.
 * Contains metadata about the note and the content of the note itself.
 */


import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinical_notes")
public class ClinicalNote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;
  private String provider;
  private Date dateCreated;
  private String noteContent;
  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  // Constructor
  public ClinicalNote(Patient patient, String provider, String noteContent) {
    this.patient = patient;
    this.provider = provider;
    this.dateCreated = new Date();
    this.noteContent = noteContent;
  }

  // no argument constructor
  public ClinicalNote() {}


  //getters and setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  // legacy accessors kept for compatibility with existing tests
  public Long  getNoteId() { return getId(); }
  public void setNoteId(Long id) { setId(id); }
  public Patient getPatient() {
    return patient;
  }
  public void setPatient(Patient patient) {
    this.patient = patient;
  }
  public String getProvider() {
    return provider;
  }
  public Date getDateCreated() {
    return dateCreated;
  }
  public String getNoteContent() {
    return noteContent;
  }
  public void setNoteContent(String noteContent) {
    this.noteContent = noteContent;
  }

}
