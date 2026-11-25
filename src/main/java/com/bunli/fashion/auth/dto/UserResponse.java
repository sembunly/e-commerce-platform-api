package com.bunli.fashion.auth.dto;

import com.bunli.fashion.auth.entity.AppUser;
import java.util.List;

public record UserResponse(Long id, String username, String email, String firstName, String lastName, String phone,
        List<String> roles, List<String> permissions) {
    public static UserResponse from(AppUser user, List<String> roles, List<String> permissions) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getPhone(), roles, permissions);
    }
}
