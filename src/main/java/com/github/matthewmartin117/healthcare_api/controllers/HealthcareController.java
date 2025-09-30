package com.github.matthewmartin117.healthcare_api.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repository.HealthcareRepository;
// mark this class as a controller
// flags it as a web controller, capable of handling HTTP requests
// returns data instead of web pages
@RestController

// Patient CRUD
@RequestMapping("/patients")
    public class HealthcareController {

        // dependency injection of the HealthcareRepository
        private final HealthcareRepository repo;
        // constructor to initialize the repository
        public HealthcareController(HealthcareRepository repo) {
            this.repo = repo;
        }

        // get all the patients
        @GetMapping
        public Collection<Patient> getAllPatients() {
            return repo.getAllPatients().values();
        }

        // get a specific patient by ID
        @GetMapping("/{patientId}")
        public Patient getPatientByID(@PathVariable String patientId) {
            return repo.getPatientByID(patientId);
        }

        // create a new patient
        @PostMapping
        public Patient createPatient(@RequestBody Patient patient) {
            repo.createPatient(patient);
            return patient;
        }

        // update an existing patient
        @PutMapping("/{id}")
        // take in the ID from the url path, and the updated patient data from the request body
        public Patient updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
            /// use the repo to update the patient
            return repo.updatePatient(id, updatedPatient);
            //patientStore.put(patientID, updatedPatient);
        }

        @DeleteMapping("/{id}")
        public String deletePatient(@PathVariable String id) {
            repo.deletePatient(id);
            return "Patient with ID " + id + " deleted.";
        }


        // Clinical notes

        // create a new clinical note for a patient
        @PostMapping("/{patientId}/clinical-notes")
        // take in the patientid through url , and the clincial note through the JSON request body
        public ClinicalNote createClinicalNote(@PathVariable String patientId , @RequestBody ClinicalNote note) {
            note.setPatientID(patientId);
            // use the repo to add the clincial note
            return repo.createClinicalNote(note);
        }

        // get all clinical notes for a patient
        @GetMapping("/{patientId}/clinical-notes")
        public List<ClinicalNote> getAllClinicalNotes(@PathVariable String patientId){
            return repo.getClinicalNotesByPatientID(patientId);
        }

        // get a specific clinical note by its ID for a patient
        @GetMapping("/{patientId}/clinical-notes/{noteId}")
        public ClinicalNote getClinicalNoteById(@PathVariable String patientId, @PathVariable String noteId) {
            return repo.getClinicalNoteByID(patientId, noteId);
        }


        // update a specific clinical note by its ID
        @PutMapping("/{patientId}/clinical-notes/{noteId}")
        public ClinicalNote updateClinicalNote(@PathVariable String patientId, @PathVariable String noteId,@RequestBody ClinicalNote updatedNote) {
            return repo.updateClinicalNote(patientId, noteId, updatedNote);
        }

        @DeleteMapping("/{patientId}/clinical-notes/{noteId}")
        public String deleteClinicalNote(@PathVariable String patientId, @PathVariable String noteId){
            repo.deleteClinicalNote(patientId, noteId);
            return "Clinical note with ID " + noteId + " for patient " + patientId + " deleted.";
        }


        // Biologfical Samples

        // create a new biological sample for a patient
        @PostMapping("/{patientId}/biological-samples")
        // take in the patientid through url , and the biological sample through the JSON request body
        public BiologicalSample createSample(@PathVariable String patientId , @RequestBody BiologicalSample sample) {
            // use the repo to add the sample
            return repo.createBiologicalSample(sample);
              }

        // get all samples for a patient
        @GetMapping("/{patientId}/biological-samples")
        public List<BiologicalSample> getAllSamples(@PathVariable String patientId){
            return repo.getBiologicalSamplesByPatientID(patientId);
        }


        // get a specific biological sample by its ID for a patient
        @GetMapping("/{patientId}/biological-samples/{sampleId}")
        public BiologicalSample getSampleById(@PathVariable String patientId, @PathVariable String sampleId) {
            return repo.getBiologicalSampleByID(patientId, sampleId);
        }


        // update a specific sample by its ID
        @PutMapping("/{patientId}/biological-samples/{sampleId}")
        public BiologicalSample updateSample(@PathVariable String patientId, @PathVariable String sampleId, @RequestBody BiologicalSample updatedSample) {
            return repo.updateBiologicalSample(patientId, sampleId, updatedSample);
        }

        @DeleteMapping("/{patientId}/biological-samples/{sampleId}")
        public String deleteSample(@PathVariable String patientId, @PathVariable String sampleId){
            repo.deleteBiologicalSample(patientId, sampleId);
            return "Biological sample with ID " + sampleId + " for patient " + patientId + " deleted.";
        }




    }
