package com.bunli.fashion.auth.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    public JwtAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                Map<String, Object> claims = authService.verifyAccessToken(header.substring(7));
                Long userId = Long.valueOf((String) claims.get("sub"));
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                addAuthorities(authorities, claims.get("roles"), "ROLE_");
                addAuthorities(authorities, claims.get("permissions"), "PERMISSION_");
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, authorities));
            } catch (IllegalArgumentException ignored) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

    private void addAuthorities(List<SimpleGrantedAuthority> authorities, Object values, String prefix) {
        if (values instanceof Collection<?> collection) {
            for (Object value : collection)
                authorities.add(new SimpleGrantedAuthority(prefix + value));
        }
    }
}
