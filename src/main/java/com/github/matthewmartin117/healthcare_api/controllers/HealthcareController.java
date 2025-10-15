package com.github.matthewmartin117.healthcare_api.controllers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.services.*;
import org.springframework.security.access.prepost.PreAuthorize;
// mark this class as a controller
// flags it as a web controller, capable of handling HTTP requests
// returns data instead of web pages
@RestController
// Patient CRUD
@RequestMapping("/patients")
    public class HealthcareController {

        // dependency injection of the patientService
        private final PatientService patientService;
        // constructor to initialize the repository
        // dependency injection of the HealthcareRepository
        private final ClinicalNoteService noteService;
        private final BiologicalSampleService sampleService;
        // constructor to initialize the repository
        public HealthcareController(PatientService patientService, ClinicalNoteService noteService,
        BiologicalSampleService sampleService) {
            this.patientService = patientService;
            this.noteService = noteService;
            this.sampleService = sampleService;
        }

        // get all the patients
        @GetMapping
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public Collection<Patient> getAllPatients() {
            return patientService.getAllPatients();
        }

        // get a specific patient by ID
        @GetMapping("/{patientId}")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public Patient getPatientByID(@PathVariable Long patientId) {
            return patientService.getPatientByID(patientId);
        }

        // create a new patient
        @PostMapping
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public Patient createPatient(@RequestBody Patient patient) {
            patientService.createPatient(patient);
            return patient;
        }

        // update an existing patient
        @PutMapping("/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        // take in the ID from the url path, and the updated patient data from the request body
        public Patient updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
            /// use the repo to update the patient
            return patientService.updatePatient(id, updatedPatient);
            //patientStore.put(patientID, updatedPatient);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public String deletePatient(@PathVariable Long id) {
            patientService.deletePatient(id);
            return "Patient with ID " + id + " deleted.";
        }


        // Clinical notes

        // create a new clinical note for a patient
        @PostMapping("/{patientId}/clinical-notes")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public ClinicalNote createClinicalNote(@PathVariable Long patientId, @RequestBody ClinicalNote note) throws Exception {
            Patient patient = patientService.getPatientByID(patientId);
            if (patient == null) {
                throw new Exception("Patient not found with id " + patientId);
            }
            note.setPatient(patient);
            return noteService.createNote(note);
        }

        // get all clinical notes for a patient
        @GetMapping("/{patientId}/clinical-notes")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public List<ClinicalNote> getAllClinicalNotes(@PathVariable Long patientId){
            return noteService.getNotesByPatientID(patientId);
        }

        // get a specific clinical note by its ID for a patient
        @GetMapping("/{patientId}/clinical-notes/{noteId}")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public ClinicalNote getClinicalNoteById(@PathVariable Long noteId) {
            return noteService.getNoteById(noteId);
        }


        // update a specific clinical note by its ID
        @PutMapping("/{patientId}/clinical-notes/{noteId}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ClinicalNote updateClinicalNote(@PathVariable Long noteId,@RequestBody ClinicalNote updatedNote) {
            return noteService.updateNote(noteId, updatedNote);
        }

        @DeleteMapping("/{patientId}/clinical-notes/{noteId}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public String deleteClinicalNote(@PathVariable Long noteId){
            noteService.deleteNote(noteId);
            return "Clinical note with ID " + noteId;
        }


        // Biological Samples

        // create a new biological sample for a patient
        @PostMapping("/{patientId}/biological-samples")
       @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public BiologicalSample createSample(@PathVariable Long patientId, @RequestBody BiologicalSample sample) throws Exception {
            Patient patient = patientService.getPatientByID(patientId);
            if(patient == null){
                throw new Exception("Patient not found with id " + patientId);
        }
            sample.setPatient(patient); // associate the patient
            return sampleService.createSample(sample);
}

        // get all samples for a patient
        @GetMapping("/{patientId}/biological-samples")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public List<BiologicalSample> getAllSamples(@PathVariable Long patientId){
            return sampleService.getSamplesByPatientId(patientId);
        }

        // get a specific biological sample by its ID for a patient
        @GetMapping("/{patientId}/biological-samples/{sampleId}")
        @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
        public BiologicalSample getSampleById(@PathVariable Long sampleId) {
            return sampleService.getSampleById(sampleId);
        }


        // update a specific sample by its ID
        @PutMapping("/{patientId}/biological-samples/{sampleId}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public BiologicalSample updateSample(@PathVariable Long sampleId, @RequestBody BiologicalSample updatedSample) {
            return sampleService.updateSample(sampleId, updatedSample);
        }

        @DeleteMapping("/{patientId}/biological-samples/{sampleId}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public String deleteSample(@PathVariable Long sampleId){
            sampleService.deleteSample(sampleId);
            return "sample deleted" + sampleId;
        }

    }
