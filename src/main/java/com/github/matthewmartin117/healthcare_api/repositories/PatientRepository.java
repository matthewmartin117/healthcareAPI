package com.github.matthewmartin117.healthcare_api.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.matthewmartin117.healthcare_api.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {

}
