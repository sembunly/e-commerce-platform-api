package com.bunli.fashion.auth.dto;

import jakarta.validation.constraints.*;
public record ForgotPasswordRequest(@NotBlank @Email String email) {}
