package com.candidature.candidature.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.UserRepository;
import com.candidature.candidature.service.AuthenticationResponse;
import com.candidature.candidature.service.AuthenticationService;
import com.candidature.candidature.service.CandidatureServiceImpl;

/**
 * @author cheikh diop
 *
 * Contrôleur pour gérer les candidatures. Il fournit des endpoints pour soumettre une candidature
 * ainsi que pour supprimer une candidature existante. Gère également le téléchargement de fichiers
 * associés aux candidatures.
 */
@RestController
public class CandidatureController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CandidatureServiceImpl candidatureServiceImp;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint pour soumettre une candidature.
     *
     * @param lastname Le nom de famille du candidat.
     * @param firstname Le prénom du candidat.
     * @param telephone Le numéro de téléphone du candidat.
     * @param sexe Le sexe du candidat.
     * @param address L'adresse du candidat.
     * @param birthdate La date de naissance du candidat.
     * @param birthplace Le lieu de naissance du candidat.
     * @param files Les fichiers à télécharger avec la candidature.
     * @param concours Le nom du concours pour lequel le candidat postule.
     * @return ResponseEntity contenant une AuthenticationResponse avec le token généré ou une erreur.
     */
    @PostMapping("/candidature")
    public ResponseEntity<AuthenticationResponse> candidature(
            @RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname,
            @RequestParam("telephone") String telephone,
            @RequestParam("sexe") String sexe,
            @RequestParam("address") String address,
            @RequestParam("birthdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate,
            @RequestParam("birthplace") String birthplace,
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("concours") String concours) {
        try {
            // Vérifier la présence de fichiers
            if (files == null || files.length == 0) {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setError("Aucun fichier sélectionné.");
                return ResponseEntity.badRequest().body(response);
            }

            // Récupérer l'utilisateur authentifié
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findUserByEmail(email);
            String userId = user.getId().toString();
            String userFolder = lastname + "_" + firstname + "_" + userId;

            // Chemin du répertoire de candidatures
            Path directory = Paths.get("candidatures/", userFolder, concours);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory); // Créez le dossier s'il n'existe pas
            }

            List<String> filePaths = new ArrayList<>(); // Pour stocker les chemins de fichiers

            // Enregistrer tous les fichiers avec leur nom d'origine
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.contains("..")) {
                    AuthenticationResponse response = new AuthenticationResponse();
                    response.setError("Nom de fichier invalide : " + originalFilename);
                    return ResponseEntity.badRequest().body(response);
                }

                Path filePath = directory.resolve(originalFilename); // Garder le nom de fichier original

                // Enregistrement du fichier
                try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    fos.write(file.getBytes());
                    filePaths.add(filePath.toString()); // Ajoutez le chemin du fichier à la liste
                } catch (IOException e) {
                    AuthenticationResponse response = new AuthenticationResponse();
                    response.setError("Erreur lors de l'enregistrement du fichier : " + originalFilename);
                    return ResponseEntity.badRequest().body(response);
                }
            }

            Candidature candidature = new Candidature();
            candidature.setFilePath(filePaths);
            AuthenticationResponse response = authenticationService.candidature(
                    lastname, firstname, telephone, sexe, address, birthdate, birthplace,
                    filePaths, concours, user);

            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setError("Erreur lors du traitement de la candidature : " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint pour supprimer une candidature par son identifiant.
     *
     * @param candidatureId L'identifiant de la candidature à supprimer.
     * @return ResponseEntity contenant un message de succès ou d'erreur.
     */
    @DeleteMapping("/candidature/delete/{id}")
    public ResponseEntity<String> deleteCandidature(@PathVariable("id") Integer candidatureId) {
        Candidature candidature = candidatureServiceImp.getCandidatureById(candidatureId);

        if (candidature == null) {
            return ResponseEntity.status(404).body("Candidature non trouvée.");
        }

        List<String> filePaths = candidature.getFilePath(); // Récupérer la liste des fichiers
        try {
            if (filePaths != null && !filePaths.isEmpty()) {
                for (String filePath : filePaths) {
                    Path path = Paths.get(filePath);
                    Files.deleteIfExists(path); // Supprimer chaque fichier
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression des fichiers.");
        }

        // Supprimer la candidature elle-même
        boolean isDeleted = candidatureServiceImp.deleteCandidatureById(candidatureId);

        if (isDeleted) {
            return ResponseEntity.ok("Candidature et fichiers supprimés avec succès.");
        } else {
            return ResponseEntity.status(500).body("Erreur lors de la suppression de la candidature.");
        }
    }
}
