package com.bunli.fashion.auth.service;

import com.bunli.fashion.auth.entity.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;

@Service
public class JwtService {
    private final byte[] secret;
    private final ObjectMapper mapper;

    public JwtService(@Value("${app.jwt.secret}") String secret, ObjectMapper mapper) {
        if (secret.length() < 32) throw new IllegalArgumentException("app.jwt.secret must be at least 32 characters");
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.mapper = mapper;
    }

    public String create(AppUser user, List<String> roles, List<String> permissions, Instant expiresAt) {
        try {
            String header = encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("sub", user.getId().toString());
            claims.put("email", user.getEmail());
            claims.put("roles", roles);
            claims.put("permissions", permissions);
            claims.put("iat", Instant.now().getEpochSecond());
            claims.put("exp", expiresAt.getEpochSecond());
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(mapper.writeValueAsBytes(claims));
            String input = header + "." + payload;
            return input + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(sign(input));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot create JWT", exception);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3 || !MessageDigest.isEqual(sign(parts[0] + "." + parts[1]), Base64.getUrlDecoder().decode(parts[2]))) {
                throw new IllegalArgumentException("Invalid signature");
            }
            Map<String, Object> claims = mapper.readValue(Base64.getUrlDecoder().decode(parts[1]), Map.class);
            if (!(claims.get("exp") instanceof Number expiry) || expiry.longValue() <= Instant.now().getEpochSecond()) {
                throw new IllegalArgumentException("Expired token");
            }
            return claims;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid access token", exception);
        }
    }

    private String encode(String value) { return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8)); }
    private byte[] sign(String value) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
    }
}
