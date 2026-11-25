package com.bunli.fashion.auth.service;

import com.bunli.fashion.auth.dto.*;
import com.bunli.fashion.auth.entity.*;
import com.bunli.fashion.auth.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.*;
import java.util.*;

@Service
public class AuthService {
    private static final String REFRESH = "REFRESH";
    private static final String PASSWORD_RESET = "PASSWORD_RESET";
    private final UserRepository users;
    private final AuthTokenRepository tokens;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwt;
    private final EntityManager entityManager;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UserRepository users, AuthTokenRepository tokens, PasswordEncoder passwordEncoder,
            JwtService jwt, EntityManager entityManager) {
        this.users = users;
        this.tokens = tokens;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
        this.entityManager = entityManager;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        String email = normaliseEmail(request.email());
        String username = request.username().trim();
        if (users.findByEmailIgnoreCase(email).isPresent() || users.findByUsernameIgnoreCase(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email or username is already in use");
        }
        AppUser user = new AppUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(blankToNull(request.firstName()));
        user.setLastName(blankToNull(request.lastName()));
        user.setPhone(blankToNull(request.phone()));
        users.saveAndFlush(user);
        int assigned = entityManager.createNativeQuery(
                "insert into user_roles (user_id, role_id) select ?1, id from roles where code='CUSTOMER' and is_active=1")
                .setParameter(1, user.getId()).executeUpdate();
        if (assigned != 1)
            throw new IllegalStateException("Default CUSTOMER role is missing or inactive");
        return issueTokens(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        AppUser user = users.findByUsernameIgnoreCase(request.username().trim())
                .orElseThrow(() -> unauthorized("Invalid username or password"));
        if (!user.isActive() || !passwordEncoder.matches(request.password(), user.getPasswordHash()))
            throw unauthorized("Invalid username or password");
        user.setLastLoginAt(Instant.now());
        return issueTokens(user);
    }

    @Transactional
    public TokenResponse refresh(String rawToken) {
        AuthToken token = usableToken(rawToken, REFRESH);
        token.revoke(); // rotate refresh token on every successful use
        return issueTokens(token.getUser());
    }

    @Transactional
    public void logout(String rawToken) {
        usableToken(rawToken, REFRESH).revoke();
    }

    @Transactional
    public void forgotPassword(String email) {
        users.findByEmailIgnoreCase(normaliseEmail(email)).filter(AppUser::isActive).ifPresent(user -> {
            String rawToken = newRandomToken();
            saveToken(user, rawToken, PASSWORD_RESET, Instant.now().plus(Duration.ofMinutes(30)));
            // An email-provider adapter should deliver rawToken. It must never be returned
            // by this endpoint.
            org.slf4j.LoggerFactory.getLogger(AuthService.class).info("Password-reset token created for user {}",
                    user.getId());
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        AuthToken resetToken = usableToken(rawToken, PASSWORD_RESET);
        AppUser user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        resetToken.markUsed();
        entityManager.createNativeQuery(
                "update auth_tokens set is_active=0, revoked_at=current_timestamp where user_id=?1 and token_type='REFRESH' and is_active=1")
                .setParameter(1, user.getId()).executeUpdate();
    }

    @Transactional(readOnly = true)
    public UserResponse currentUser(long userId) {
        AppUser user = users.findById(userId).filter(AppUser::isActive)
                .orElseThrow(() -> unauthorized("User is unavailable"));
        return UserResponse.from(user, rolesFor(user.getId()), permissionsFor(user.getId()));
    }

    public Map<String, Object> verifyAccessToken(String rawToken) {
        return jwt.verify(rawToken);
    }

    private TokenResponse issueTokens(AppUser user) {
        if (!user.isActive())
            throw unauthorized("User is unavailable");
        List<String> roles = rolesFor(user.getId());
        List<String> permissions = permissionsFor(user.getId());
        Instant accessExpiry = Instant.now().plus(Duration.ofMinutes(15));
        String refreshToken = newRandomToken();
        saveToken(user, refreshToken, REFRESH, Instant.now().plus(Duration.ofDays(30)));
        return new TokenResponse(jwt.create(user, roles, permissions, accessExpiry), refreshToken, "Bearer", 900,
                UserResponse.from(user, roles, permissions));
    }

    @SuppressWarnings("unchecked")
    private List<String> rolesFor(Long userId) {
        return entityManager.createNativeQuery(
                "select r.code from roles r join user_roles ur on ur.role_id=r.id where ur.user_id=?1 and r.is_active=1 and ur.is_active=1")
                .setParameter(1, userId).getResultList();
    }

    @SuppressWarnings("unchecked")
    private List<String> permissionsFor(Long userId) {
        return entityManager.createNativeQuery(
                "select distinct md.code from module_detail md join role_permissions rp on rp.module_detail_id=md.id join user_roles ur on ur.role_id=rp.role_id where ur.user_id=?1 and ur.is_active=1 and rp.is_active=1 and md.is_active=1")
                .setParameter(1, userId).getResultList();
    }

    private void saveToken(AppUser user, String rawToken, String type, Instant expiresAt) {
        AuthToken token = new AuthToken();
        token.setUser(user);
        token.setTokenHash(hash(rawToken));
        token.setTokenType(type);
        token.setExpiresAt(expiresAt);
        tokens.save(token);
    }

    private AuthToken usableToken(String rawToken, String type) {
        return tokens.findByTokenHashAndTokenType(hash(rawToken), type)
                .filter(token -> token.getUser().isActive() && token.isUsable())
                .orElseThrow(() -> unauthorized("Invalid or expired token"));
    }

    private String newRandomToken() {
        byte[] bytes = new byte[48];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            return HexFormat.of()
                    .formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String normaliseEmail(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }
}
