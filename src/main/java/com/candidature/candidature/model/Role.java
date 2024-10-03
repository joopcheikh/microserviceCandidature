package com.candidature.candidature.model;

/**
 * @author cheikh diop, sosthene
 *
 * Cette énumération représente les différents rôles d'utilisateur
 * dans le système de gestion des candidatures.
 *
 * Les rôles possibles sont :
 * - ADMIN : Utilisateur ayant des droits d'administrateur,
 *   avec un accès complet aux fonctionnalités du système.
 * - USER : Utilisateur standard, avec des permissions limitées,
 *   généralement pour postuler et gérer ses candidatures.
 * - RECRUITER : Utilisateur chargé de la gestion des candidatures,
 *   avec des droits spécifiques pour examiner et décider des
 *   candidatures soumises.
 *
 * Cette énumération permet de définir clairement les niveaux
 * d'accès et les responsabilités des utilisateurs au sein de
 * l'application.
 */
public enum Role {
    ADMIN,     // Rôle d'administrateur
    USER,      // Rôle d'utilisateur standard
    RECRUITER  // Rôle de recruteur
}
