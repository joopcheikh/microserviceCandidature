package com.candidature.candidature.model;

/**
 * @author cheikh diop, sosthene
 *
 * Cette énumération représente les différents statuts d'une candidature
 * dans le système de gestion des candidatures.
 *
 * Les statuts possibles sont :
 * - ACCEPTED : La candidature a été acceptée.
 * - REJECTED : La candidature a été rejetée.
 * - WAITING : La candidature est en attente d'une décision.
 *
 * Cette énumération permet de standardiser les états des candidatures
 * et d'assurer une gestion cohérente des processus décisionnels.
 */
public enum Status {
    ACCEPTED,  // Candidature acceptée
    REJECTED,  // Candidature rejetée
    WAITING    // Candidature en attente
}
