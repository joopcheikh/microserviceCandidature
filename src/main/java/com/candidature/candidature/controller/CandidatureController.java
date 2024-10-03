package com.candidature.candidature.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.ArrayList;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

                // Enregistrement du fichier avec un flux d'entrée
                try (InputStream inputStream = file.getInputStream();
                        FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
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



    private static final String CANDIDATURES_FOLDER_PATH = "candidatures/";

    @GetMapping("/download-all-candidatures")
    public ResponseEntity<InputStreamResource> downloadAllCandidatures() {
        try {
            // Créer un fichier temporaire ZIP dans lequel toutes les candidatures seront zippées
            File zipFile = File.createTempFile("candidatures_", ".zip");

            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
                // Parcourir tous les fichiers du dossier des candidatures
                Files.walk(Paths.get(CANDIDATURES_FOLDER_PATH))
                        .filter(Files::isRegularFile) // On ne prend que les fichiers, pas les dossiers
                        .forEach(filePath -> {
                            try {
                                // Ajouter chaque fichier au ZIP
                                ZipEntry zipEntry = new ZipEntry(filePath.toString().replace(CANDIDATURES_FOLDER_PATH, ""));
                                zipOut.putNextEntry(zipEntry);
                                Files.copy(filePath, zipOut);
                                zipOut.closeEntry();
                            } catch (IOException e) {
                                e.printStackTrace(); // Gérer l'exception si un fichier ne peut pas être ajouté
                            }
                        });
            }

            // Préparer la réponse HTTP
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidatures.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
