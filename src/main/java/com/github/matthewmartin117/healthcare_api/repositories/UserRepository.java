package com.github.matthewmartin117.healthcare_api.repositories;

import com.github.matthewmartin117.healthcare_api.models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// repository interface for User entity
// gives us standard CRUD operations and custom queries
// can find user by username
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
