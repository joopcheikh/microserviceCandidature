package com.candidature.candidature.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.candidature.candidature.model.User;

/**
 * @author cheikh diop
 *
 * L'interface `UserRepository` permet d'effectuer des opérations CRUD sur
 * les entités `User`. Elle hérite des méthodes fournies par `JpaRepository`
 * pour faciliter l'interaction avec la base de données.
 *
 * Méthodes disponibles :
 * - `findUserByEmail(String email)` : Recherche un utilisateur par son adresse email.
 *
 * Utilisation :
 * - Cette interface est utilisée dans le service d'authentification pour gérer les
 *   opérations liées aux utilisateurs, telles que l'inscription, la connexion et la
 *   vérification de l'existence d'un utilisateur.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Trouve un utilisateur en fonction de son adresse email.
     *
     * @param email L'adresse email de l'utilisateur à rechercher.
     * @return L'utilisateur correspondant à l'adresse email, ou null si aucun
     *         utilisateur n'est trouvé.
     */
    User findUserByEmail(String email);
}
