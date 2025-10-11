package com.github.matthewmartin117.healthcare_api.models;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.github.matthewmartin117.healthcare_api.repositories.UserRepository;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadUsers(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                admin.setRoles("ROLE_ADMIN,ROLE_USER");
                userRepo.save(admin);
            }
        };
    }
}
