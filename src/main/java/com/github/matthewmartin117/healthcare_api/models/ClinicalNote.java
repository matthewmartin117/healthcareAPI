package com.github.matthewmartin117.healthcare_api.models;
/* This class represents a clinical note about a patient.
 * Contains metadata about the note and the content of the note itself.
 */

import java.util.Date;
import java.util.UUID;
import java.util.*;

public class ClinicalNote {
  private String id;
  private String patientID;
  private String provider;
  private Date dateCreated;
  private String noteContent;

  // Constructor
  public ClinicalNote(String patientID, String provider, String noteContent) {
    this.id = UUID.randomUUID().toString();
    this.patientID = patientID;
    this.provider = provider;
    this.dateCreated = new Date();
    this.noteContent = noteContent;
  }

  //getters and setters
  public String getId() { return id; }
  public String getPatientID() {
    return patientID;
  }
  public void setPatientID(String patientID) {
    this.patientID = patientID;
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
