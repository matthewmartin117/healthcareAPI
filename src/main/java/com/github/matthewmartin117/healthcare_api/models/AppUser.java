package com.github.matthewmartin117.healthcare_api.models;
// import org.hibernate.annotations.Table; // Removed deprecated import
import jakarta.persistence.Table;

// User entity for authentication and authorization
// this is a representation of a user who uses the healthcare system
// contains fields for username, password, roles, and other relevant user information
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Data
@NoArgsConstructor
@Table(name = "appusers")
public class AppUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;
  private String password; // In a real application, passwords should be hashed
  private String roles; // "ROLE_ADMIN,ROLE_USER"

   public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null || roles.isEmpty()) return Collections.emptyList();
    return Arrays.stream(roles.split(","))
                 .map(SimpleGrantedAuthority::new)
                 .collect(Collectors.toList());
}

public void setRoles(String roles) {
        this.roles = roles;
    }


    public String getUsername() {
        return username;
    }
}
