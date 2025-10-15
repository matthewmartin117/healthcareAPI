package com.github.matthewmartin117.healthcare_api.models;
/*This clas is a representation of a biological sample collected from a patient
 * Includes metadata about the sample and its type
 */

import java.util.Date;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "biological_samples")
public class BiologicalSample {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;
  private String sampleType; // e.g. blood, urine, tissue
  private Date collectionDate; // Date the sample was collected
  private String reasonCollected;

  @ManyToOne
  @JoinColumn(name = "patient_id", nullable = false)
  private Patient patient;

  // Constructor
  public BiologicalSample(Patient patient, String sampleType, Date collectionDate, String reasonCollected) {
    this.patient = patient;
    this.sampleType = sampleType;
    this.collectionDate = collectionDate;
    this.reasonCollected = reasonCollected;
    }
// no parameter constructor
public BiologicalSample(){

}
    //getters and setters

  public Long getSampleId() { return Id; }
  public void setSampleId(Long Id) {this.Id =Id;}

  public Patient getPatient() {
    return patient;
  }
  public void setPatient(Patient patient) {
    this.patient = patient;
  }
  public String getSampleType() {
    return sampleType;
  }
  public void setSampleType(String type){
    this.sampleType = type;
  }
  public Date getCollectionDate() {
    return collectionDate;
  }
  public void setCollectionDate(Date collectionDate){
    this.collectionDate = collectionDate;
  }
  public String getReasonCollected() {
    return reasonCollected;
  }
  public void setReasonCollected(String reasonCollected) {
    this.reasonCollected = reasonCollected;
  }

}