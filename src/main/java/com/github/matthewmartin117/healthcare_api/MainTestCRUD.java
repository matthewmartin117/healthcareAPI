package com.github.matthewmartin117.healthcare_api;

import java.sql.Date;
import java.util.UUID;

import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;
import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repository.HealthcareRepository;

import java.util.*;

public class MainTestCRUD {
    public static void main(String[] args) {
        // Initialize repository
        HealthcareRepository repo = new HealthcareRepository();

        // =====================
        // CREATE PATIENT
        // =====================
        Patient patient = new Patient(
            "p001",
            "John Doe",
            Date.valueOf("1990-01-01"),
            Map.of("phone", "555-1234", "email", "john@example.com")
        );
        repo.createPatient(patient);

        // =====================
        // CREATE BIOLOGICAL SAMPLE
        // =====================
        String sampleId = UUID.randomUUID().toString();  // generate unique ID
        BiologicalSample sample = new BiologicalSample(
            sampleId,
            "p001",
            "blood",
            Date.valueOf("2025-09-26"),
            "routine test"
        );
        repo.createBiologicalSample(sample);

        // =====================
        // CREATE CLINICAL NOTE
        // =====================
        String noteId = UUID.randomUUID().toString(); // generate unique ID
        ClinicalNote note = new ClinicalNote(
            noteId,
            "p001",
            "Dr. Smith",
            Date.valueOf("2025-09-26"),
            "Patient is healthy"
        );
        repo.createClinicalNote(note);

        // =====================
        // READ OPERATIONS
        // =====================
        System.out.println("All patients:");
        System.out.println(repo.getAllPatients());

        System.out.println("\nBiological samples for patient p001:");
        List<BiologicalSample> samples = repo.getBiologicalSamplesByPatientID("p001");
        samples.forEach(s -> System.out.println(s.getId() + ": " + s.getSampleType()));

        System.out.println("\nClinical notes for patient p001:");
        List<ClinicalNote> notes = repo.getClinicalNotesByPatientID("p001");
        notes.forEach(n -> System.out.println(n.getId() + ": " + n.getNoteContent()));

        // =====================
        // UPDATE OPERATIONS
        // =====================
        BiologicalSample updatedSample = new BiologicalSample(
            sampleId,
            "p001",
            "blood",
            Date.valueOf("2025-09-26"),
            "updated reason"
        );
        repo.updateBiologicalSample("p001", sampleId, updatedSample);

        ClinicalNote updatedNote = new ClinicalNote(
            noteId,
            "p001",
            "Dr. Smith",
            Date.valueOf("2025-09-26"),
            "Updated note content"
        );
        repo.updateClinicalNote("p001", noteId, updatedNote);

        // =====================
        // DELETE OPERATIONS
        // =====================
        repo.deleteBiologicalSample("p001", sampleId);
        repo.deleteClinicalNote("p001", noteId);

        System.out.println("\nAfter deletion, samples and notes:");
        System.out.println("Samples: " + repo.getBiologicalSamplesByPatientID("p001").size());
        System.out.println("Notes: " + repo.getClinicalNotesByPatientID("p001").size());
    }
}
