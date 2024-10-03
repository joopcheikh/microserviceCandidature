package com.candidature.candidature.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.CandidatureRepository;
import com.candidature.candidature.repository.UserRepository;

import io.jsonwebtoken.io.IOException;

/**
 * @author cheikh diop, sosthene
 *
 * La classe `AuthenticationService` gère le processus d'authentification et de
 * candidature des utilisateurs. Elle fournit des méthodes pour enregistrer
 * des candidatures et générer des tokens JWT pour les utilisateurs.
 */
@Service
public class AuthenticationService {

    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            CandidatureRepository candidatureRepository,
            AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.candidatureRepository = candidatureRepository;
    }

    /**
     * Enregistre une candidature pour un utilisateur donné.
     *
     * @param lastname     Le nom de famille du candidat.
     * @param firstname    Le prénom du candidat.
     * @param telephone    Le numéro de téléphone du candidat.
     * @param sexe         Le sexe du candidat.
     * @param address      L'adresse du candidat.
     * @param birthdate    La date de naissance du candidat.
     * @param birthplace   Le lieu de naissance du candidat.
     * @param filePaths    Une liste de chemins vers les fichiers de candidature
     *                     (doivent être des fichiers PDF).
     * @param concours     Le nom du concours auquel le candidat postule.
     * @param user         L'utilisateur qui soumet la candidature.
     * @return            Une réponse d'authentification contenant un token JWT et le rôle de l'utilisateur.
     * @throws IOException Si un fichier est introuvable ou n'est pas un PDF.
     */
    public AuthenticationResponse candidature(
            String lastname,
            String firstname,
            String telephone,
            String sexe,
            String address,
            Date birthdate,
            String birthplace,
            List<String> filePaths, // Change from String[] to List<String>
            String concours,
            User user
    ) throws IOException {

        // Vérification que tous les fichiers existent et sont des PDFs
        for (String filePath : filePaths) {
            Path file = Paths.get(filePath);
            if (!Files.exists(file)) {
                throw new IllegalArgumentException("Le fichier suivant n'existe pas : " + filePath);
            }
            if (!filePath.endsWith(".pdf")) {
                throw new IllegalArgumentException("Le fichier doit être un PDF : " + filePath);
            }
        }

        // Création de l'objet Candidature
        Candidature candidat = new Candidature();
        candidat.setLastname(lastname);
        candidat.setFirstname(firstname);
        candidat.setTelephone(telephone);
        candidat.setSexe(sexe);
        candidat.setAdress(address);
        candidat.setBirthdate(birthdate);
        candidat.setBirthplace(birthplace);
        candidat.setFilePath(filePaths); // Stocker la liste de chemins de fichiers
        candidat.setConcours(concours);
        candidat.setUser(user);

        // Enregistrement de la candidature et de l'utilisateur
        try {
            candidatureRepository.save(candidat);
            user.setHave_postuled(true);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la candidature.", e);
        }

        // Génération du token
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token, user.getRole().name(), "");
    }
}
