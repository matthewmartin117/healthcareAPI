package com.github.matthewmartin117.healthcare_api;
/*This clas is a representation of a biological sample collected from a patient
 * Includes metadata about the sample and its type
 */

import java.sql.Date;

public class BiologicalSample {
  private String id;
  private String patientID;
  private String sampleType; // e.g. blood, urine, tissue
  private Date collectionDate; // Date the sample was collected
  private String reasonCollected;

  // Constructor
  public BiologicalSample(String patientID, String sampleType, Date collectionDate, String reasonCollected) {
    this.patientID = patientID;
    this.sampleType = sampleType;
    this.collectionDate = collectionDate;
    this.reasonCollected = reasonCollected;
    }

    //getters and setters

  public String getPatientID() {
    return patientID;
  }
  public String getSampleType() {
    return sampleType;
  }
  public Date getCollectionDate() {
    return collectionDate;
  }
  public String getReasonCollected() {
    return reasonCollected;
  }
  public void setReasonCollected(String reasonCollected) {
    this.reasonCollected = reasonCollected;
  }

}