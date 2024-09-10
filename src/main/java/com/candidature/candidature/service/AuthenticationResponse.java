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
    /*
     * La classe AuthenticationResponse sert à encapsuler et structurer
     * la réponse qui sera renvoyée au client après une opération d'authentification ou d'enregistrement réussie.
     */
}
