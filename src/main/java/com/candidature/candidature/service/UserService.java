package com.candidature.candidature.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.candidature.candidature.model.User;

/**
 * @author cheikh diop
 *
 * Interface de service pour la gestion des utilisateurs. Cette interface définit les méthodes
 * nécessaires pour ajouter, mettre à jour, lister et supprimer des utilisateurs dans le système.
 */
@Service
public interface UserService {

    /**
     * Ajoute un nouvel utilisateur au système.
     *
     * @param user L'objet User contenant les informations de l'utilisateur à ajouter.
     * @return L'utilisateur ajouté avec les informations mises à jour (par exemple, l'ID généré).
     */
    public User addUser(User user);

    /**
     * Met à jour les informations d'un utilisateur existant.
     *
     * @param user L'objet User contenant les nouvelles informations de l'utilisateur.
     * @return L'utilisateur mis à jour.
     */
    public User updateUsers(User user);

    /**
     * Récupère la liste de tous les utilisateurs dans le système.
     *
     * @return Une liste d'objets User représentant tous les utilisateurs.
     */
    public List<User> listerUsers();

    /**
     * Supprime un utilisateur du système par son ID.
     *
     * @param id L'identifiant de l'utilisateur à supprimer.
     */
    public void deleteUsers(Integer id);
}
