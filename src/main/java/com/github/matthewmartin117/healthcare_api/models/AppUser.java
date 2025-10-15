package com.github.matthewmartin117.healthcare_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "appusers")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password; // already encoded
    private String roles; // just comma separated: "ADMIN,USER"

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) return Collections.emptyList();
        // strip ROLE_ prefix, matches hasAuthority('ADMIN')
        return Arrays.stream(roles.split(","))
                     .map(String::trim)
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }

}

