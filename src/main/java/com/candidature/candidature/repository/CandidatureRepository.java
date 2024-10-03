package com.candidature.candidature.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candidature.candidature.model.Candidature;

/**
 * @author cheikh diop
 *
 * L'interface `CandidatureRepository` permet d'effectuer des opérations CRUD sur
 * les entités `Candidature`. Elle hérite des méthodes fournies par `JpaRepository`
 * pour faciliter l'interaction avec la base de données.
 *
 * Méthodes disponibles :
 * - `findByUserId(Integer userId)` : Récupère toutes les candidatures soumises
 *   par un utilisateur donné, identifié par son identifiant.
 *
 * Utilisation :
 * - Cette interface est utilisée dans le service de candidature pour gérer les
 *   opérations liées aux candidatures, telles que la récupération des candidatures
 *   d'un utilisateur spécifique.
 */
public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {

    /**
     * Trouve toutes les candidatures associées à un utilisateur par son identifiant.
     *
     * @param userId Identifiant de l'utilisateur.
     * @return Liste des candidatures soumises par l'utilisateur.
     */
    List<Candidature> findByUserId(Integer userId);
}
