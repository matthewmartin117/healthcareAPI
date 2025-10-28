package com.github.matthewmartin117.healthcare_api.controllers;
import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.services.BiologicalSampleService;
import com.github.matthewmartin117.healthcare_api.services.ClinicalNoteService;
import com.github.matthewmartin117.healthcare_api.services.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class HealthcareController {

    private final PatientService patientService;
    private final ClinicalNoteService noteService;
    private final BiologicalSampleService sampleService;

    public HealthcareController(PatientService patientService, ClinicalNoteService noteService,
                                BiologicalSampleService sampleService) {
        this.patientService = patientService;
        this.noteService = noteService;
        this.sampleService = sampleService;
    }

    // Patients
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Collection<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Patient getPatientByID(@PathVariable Long patientId) {
        return patientService.getPatientByID(patientId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        return patientService.updatePatient(id, updatedPatient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "Patient with ID " + id + " deleted.";
    }

    // Clinical Notes
    @PostMapping("/{patientId}/clinical-notes")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClinicalNote createClinicalNote(@PathVariable Long patientId, @RequestBody ClinicalNote note) throws Exception {
        Patient patient = patientService.getPatientByID(patientId);
        if (patient == null) throw new Exception("Patient not found with id " + patientId);
        note.setPatient(patient);
        return noteService.createNote(note);
    }

    @GetMapping("/{patientId}/clinical-notes")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<ClinicalNote> getAllClinicalNotes(@PathVariable Long patientId){
        return noteService.getNotesByPatientID(patientId);
    }

    @GetMapping("/{patientId}/clinical-notes/{noteId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClinicalNote getClinicalNoteById(@PathVariable Long noteId) {
        return noteService.getNoteById(noteId);
    }

    @PutMapping("/{patientId}/clinical-notes/{noteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClinicalNote updateClinicalNote(@PathVariable Long noteId, @RequestBody ClinicalNote updatedNote) {
        return noteService.updateNote(noteId, updatedNote);
    }

    @DeleteMapping("/{patientId}/clinical-notes/{noteId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteClinicalNote(@PathVariable Long noteId){
        noteService.deleteNote(noteId);
        return "Clinical note with ID " + noteId + " deleted.";
    }

    // Biological Samples
    @PostMapping("/{patientId}/biological-samples")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BiologicalSample createSample(@PathVariable Long patientId, @RequestBody BiologicalSample sample) throws Exception {
        Patient patient = patientService.getPatientByID(patientId);
        if(patient == null) throw new Exception("Patient not found with id " + patientId);
        sample.setPatient(patient);
        return sampleService.createSample(sample);
    }

    @GetMapping("/{patientId}/biological-samples")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<BiologicalSample> getAllSamples(@PathVariable Long patientId){
        return sampleService.getSamplesByPatientId(patientId);
    }

    @GetMapping("/{patientId}/biological-samples/{sampleId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BiologicalSample getSampleById(@PathVariable Long sampleId) {
        return sampleService.getSampleById(sampleId);
    }

    @PutMapping("/{patientId}/biological-samples/{sampleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public BiologicalSample updateSample(@PathVariable Long sampleId, @RequestBody BiologicalSample updatedSample) {
        return sampleService.updateSample(sampleId, updatedSample);
    }

    @DeleteMapping("/{patientId}/biological-samples/{sampleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSample(@PathVariable Long sampleId){
        sampleService.deleteSample(sampleId);
        return "Sample deleted: " + sampleId;
    }

    // health check endpoint
    @GetMapping("/")
    public String healthCheck() {
        // Returns a simple string and a 200 OK
        return "Application is running.";
    }
}
