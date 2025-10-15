package com.github.matthewmartin117.healthcare_api.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;

@Service
public class PatientService {

  private final PatientRepository patientRepo;

  public PatientService(PatientRepository patientRepo){
    this.patientRepo  = patientRepo;
  }

  public Patient createPatient(Patient patient){
    return patientRepo.save(patient);
  }

  public Patient getPatientByID(Long id){
    return patientRepo.findById(id).orElse( null);
  }

  public Patient updatePatient(Long id, Patient updatedPatient){
    updatedPatient.setPatientID(id);
    return patientRepo.save(updatedPatient);
  }

  public void deletePatient(Long id){
    patientRepo.deleteById(id);
  }

  public List<Patient> getAllPatients(){
    return patientRepo.findAll();
  }


}