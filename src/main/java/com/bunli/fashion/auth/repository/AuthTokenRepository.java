package com.bunli.fashion.auth.repository;

import com.bunli.fashion.auth.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByTokenHashAndTokenType(String tokenHash, String tokenType);
}
