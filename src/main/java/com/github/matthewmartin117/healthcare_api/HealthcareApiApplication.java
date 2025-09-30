package com.github.matthewmartin117.healthcare_api;

import java.sql.Date;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.matthewmartin117.healthcare_api.models.Patient;
import com.github.matthewmartin117.healthcare_api.repository.HealthcareRepository;

@SpringBootApplication
public class HealthcareApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcareApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(HealthcareRepository repo){
		return args -> {
			// create a sample patient
			Patient p1 = new Patient("1","John Doe", Date.valueOf("1998-12-20"), Map.of("phone", "555-1234", "email", "john@example.com"));
			repo.createPatient(p1);
			// Read
			System.out.println("Patient with ID 1: " + repo.getPatientByID("1"));
			System.out.println("All Patients: " + repo.getAllPatients());
			// Update
			Patient updated = new Patient("1", "John Doe", Date.valueOf("1993-11-12"), Map.of("phone", "555-5678", "email", "john.doe@example.com"));
			repo.updatePatient("1", updated);
			System.out.println("Updated Patient with ID 1: " + repo.getPatientByID("1"));

			// Delete
			repo.deletePatient("1");
			System.out.println("All Patients after deletion: " + repo.getAllPatients());
		};
	}

}
