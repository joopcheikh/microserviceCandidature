package com.candidature.candidature.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
    private String error;


    // Getters and Setters
}
