package com.github.matthewmartin117.healthcare_api.models;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;
import com.github.matthewmartin117.healthcare_api.repositories.PatientRepository;
import com.github.matthewmartin117.healthcare_api.models.AppUser;
import com.github.matthewmartin117.healthcare_api.models.Patient;
// load initial data into the database - used for testing purposes
@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadUsers(UserRepository userRepo, PasswordEncoder passwordEncoder, PatientRepository patientRepo) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                // create an admin user
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                admin.setRoles("ADMIN");
                userRepo.save(admin);
                System.out.println("Admin user created: admin/adminpassword");
                // create a regular user
                AppUser user = new AppUser();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("userpassword"));
                user.setRoles("USER");
                userRepo.save(user);
                System.out.println("Regular user created: user/userpassword");

                // creeate a patient
                Patient patient = new Patient();
                patient.setName("John Doe");
                // save patient
                patientRepo.save(patient);
                System.out.println("Test patient created: John Doe");
                // creeate a second
                Patient patient2 = new Patient();
                patient2.setName("Jane Smith");
                // save patient
                patientRepo.save(patient2);
                System.out.println("Test patient created: Jane Smith");

            }
        };
    }
}
