package com.candidature.candidature.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
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

@RestController
public class CandidatureController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CandidatureServiceImpl candidatureServiceImp;

    @Autowired
    private UserRepository userRepository;

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
            if (files == null || files.length == 0) {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setError("Aucun fichier sélectionné.");
                return ResponseEntity.badRequest().body(response);
            }
    
            // Récupérer l'authentification de l'utilisateur
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
