package com.candidature.candidature.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String role;
    private String error;

    public AuthenticationResponse(String token, String role, String error) {
        this.token = token;
        this.role = role;
        this.error = error;
    }

    // Getters and Setters
}
