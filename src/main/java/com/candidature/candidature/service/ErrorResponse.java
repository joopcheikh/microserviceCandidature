package com.candidature.candidature.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cheikh diop
 *
 * Classe `ErrorResponse` utilisée pour encapsuler les messages d'erreur renvoyés par l'API.
 * Cette classe facilite la gestion des erreurs en standardisant la structure des réponses d'erreur.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    /**
     * Message d'erreur décrivant le problème survenu.
     */
    private String message;

}
