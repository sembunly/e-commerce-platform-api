package com.bunli.fashion.auth.dto;

public record TokenResponse(String accessToken, String refreshToken, String tokenType, long expiresIn, UserResponse user) {}
