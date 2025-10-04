package com.github.matthewmartin117.healthcare_api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.matthewmartin117.healthcare_api.models.BiologicalSample;
import java.util.*;
public interface BiologicalSampleRepository extends JpaRepository<BiologicalSample, String> {
    // This tells Spring Data JPA to find all samples for a given patientId
    List<BiologicalSample> findByPatient_PatientID(String patientID);
}
