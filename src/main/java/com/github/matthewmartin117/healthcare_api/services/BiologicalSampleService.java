package com.github.matthewmartin117.healthcare_api.services;
import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.repositories.BiologicalSampleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class BiologicalSampleService {

  private final BiologicalSampleRepository sampleRepo;


  public BiologicalSampleService(BiologicalSampleRepository sampleRepo){
    this.sampleRepo = sampleRepo;
  }

   // Create sample
    public BiologicalSample createSample(BiologicalSample sample) {
        return sampleRepo.save(sample);
    }

    // Get all samples for a patient
    public List<BiologicalSample> getSamplesByPatientId(String patientId) {
        return sampleRepo.findByPatient_PatientID(patientId);
    }

    // Get a single sample by ID
    public BiologicalSample getSampleById(String sampleId) {
        return sampleRepo.findById(sampleId).orElse(null);
    }

    // Update sample
    public BiologicalSample updateSample(String sampleId, BiologicalSample updatedSample) {
        return sampleRepo.findById(sampleId)
            .map(sample -> {
                sample.setSampleType(updatedSample.getSampleType());
                sample.setReasonCollected(updatedSample.getReasonCollected());
                return sampleRepo.save(sample);
            }).orElse(null);
    }

     // Delete sample
    public void deleteSample(String sampleId) {
        sampleRepo.deleteById(sampleId);
    }

}
