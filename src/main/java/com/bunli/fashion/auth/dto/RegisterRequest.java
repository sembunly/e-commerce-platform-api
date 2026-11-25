package com.bunli.fashion.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank @Size(min = 3, max = 100) String username,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @Size(max = 100) String firstName, @Size(max = 100) String lastName,
        @Size(max = 30) String phone) {
}
