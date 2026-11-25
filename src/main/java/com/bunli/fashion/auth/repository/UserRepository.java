package com.bunli.fashion.auth.repository;

import com.bunli.fashion.auth.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);

    Optional<AppUser> findByUsernameIgnoreCase(String username);
}
