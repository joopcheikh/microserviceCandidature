package com.candidature.candidature.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String role;
    private String error; // Ajout d'un champ pour les messages d'erreur
}