package com.candidature.candidature.service;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cheikh diop
 *
 * La classe `AuthenticationResponse` représente la réponse d'authentification
 * envoyée aux utilisateurs après une tentative de connexion. Elle contient les
 * informations essentielles concernant le résultat de l'authentification.
 *
 * Attributs :
 * - `token` : Le token JWT généré lors de la connexion, utilisé pour l'authentification
 *   des requêtes ultérieures.
 * - `role` : Le rôle de l'utilisateur (par exemple, ADMIN, USER) déterminant ses
 *   permissions au sein de l'application.
 * - `error` : Un message d'erreur, le cas échéant, qui fournit des détails sur
 *   les raisons d'un échec d'authentification.
 */
@Data
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;   // Le token JWT pour l'authentification
    private String role;    // Le rôle de l'utilisateur
    private String error;   // Message d'erreur en cas d'échec de l'authentification

    /**
     * Constructeur pour créer une réponse d'authentification avec des valeurs
     * spécifiées pour le token, le rôle et l'erreur.
     *
     * @param token Le token JWT généré.
     * @param role  Le rôle de l'utilisateur.
     * @param error Un message d'erreur, si applicable.
     */
    public AuthenticationResponse(String token, String role, String error) {
        this.token = token;
        this.role = role;
        this.error = error;
    }

    // Les getters et setters sont générés automatiquement par Lombok grâce à l'annotation @Data.
}
