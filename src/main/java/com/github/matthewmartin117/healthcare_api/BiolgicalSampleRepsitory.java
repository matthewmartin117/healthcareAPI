package com.github.matthewmartin117.healthcare_api;
import java.util.List;
public class BiolgicalSampleRepsitory {
  // this is a simple class the stores Biological samples in-memory
  // This stores biological samples according to their patientID
  // each patientID can have multiple biological samples
  private java.util.Map< String, List <BiologicalSample> > biologicalSampleStore;

  // constructor
  public BiolgicalSampleRepsitory(java.util.Map< String, List <BiologicalSample> > biologicalSampleStore) {
    this.biologicalSampleStore = biologicalSampleStore;
  }

  // Create
  // ceateBiologicalSample takes a BiologicalSample object and adds it to the biologicalSampleStore
  public void createBiologicalSample(BiologicalSample sample){
    // take a biliogical sample and add it to the biologicalSampleStore , using patientID
    String patientID = sample.getPatientID();
     // if the patient ID exists in the store, add the sample to the list
    if (biologicalSampleStore.containsKey(patientID)){
      biologicalSampleStore.get(patientID).add(sample);
    } else {
      // if the patient ID does not exist, create a new list and add the sample
      List<BiologicalSample> newSampleList = new java.util.ArrayList<>();
      newSampleList.add(sample);
      biologicalSampleStore.put(patientID, newSampleList);
     }
  }
  // Read
  // getBiologicalSamplesByPatientID looks up a patient ID and returns the corresponding list of biological samples
  public List<BiologicalSample> getBiologicalSamplesByPatientID(String patientID){
    return biologicalSampleStore.get(patientID);
  }
  // Update
  // updateBiologicalSample takes a patient ID, an index of the sample in the list, and an updated BiologicalSample object
  // it updates the existing biological sample record with the new biological sample information
  public void updateBiologicalSample(String patientID, int sampleIndex, BiologicalSample updatedSample){
    if (biologicalSampleStore.containsKey(patientID)){
      // retrive the list of samples for the patient
      List<BiologicalSample> samples = biologicalSampleStore.get(patientID);
      // if the sample index is valid, update the sample at that index
      if (sampleIndex >= 0 && sampleIndex < samples.size()){
        // set the sample at the specified index to the updated sample
        samples.set(sampleIndex, updatedSample);
      }
    }
  }

  // Delete
  // deleteBiologicalSample removes a biological sample from the biologicalSampleStore based on their patientID and sample index
  public void deleteBiologicalSample(String patientID, int sampleIndex){
    if (biologicalSampleStore.containsKey(patientID)){
      // remove the sample at the specified index if the index is valid
      if (sampleIndex >= 0 && sampleIndex < biologicalSampleStore.get(patientID).size()){
        // remove the sample at the specified index
        biologicalSampleStore.get(patientID).remove(sampleIndex);
      }
    }
  }
}