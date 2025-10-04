package com.github.matthewmartin117.healthcare_api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.github.matthewmartin117.healthcare_api.models.ClinicalNote;

public interface ClinicalNoteRepository extends JpaRepository<ClinicalNote, String> {
  List<ClinicalNote> findByPatient_PatientID(String patientID);
}
