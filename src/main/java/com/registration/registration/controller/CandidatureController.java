package com.registration.registration.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.registration.registration.service.AuthenticationService;

@RestController
public class CandidatureController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/candidature")
    public ResponseEntity<String> candidature(
            @RequestParam("username") String username,
            @RequestParam("firstname") String firstname,
            @RequestParam("telephone") String telephone,
            @RequestParam("sexe") String sexe,
            @RequestParam("address") String address, // Correction du mot "adress" en "address"
            @RequestParam("birthdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate, // Assurez-vous d'importer @DateTimeFormat
            @RequestParam("birthplace") String birthplace,
            @RequestParam("cnicardnumber") String cnicardnumber,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // Vérifiez que le fichier n'est pas null
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Aucun fichier sélectionné.");
            }
    
            // Définissez le répertoire où vous voulez stocker les fichiers
            Path directory = Paths.get("candidatures/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
    
            // Assurez-vous que le nom du fichier est sûr et valide
            String filename = file.getOriginalFilename();
            if (filename == null || filename.contains("..")) {
                return ResponseEntity.badRequest().body("Nom de fichier invalide.");
            }
    
            Path path = directory.resolve(filename);
    
            // Enregistrez le fichier sur le serveur
            try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Erreur lors de l'enregistrement du fichier.");
            }
    
            // Passez le chemin du fichier au service
            String filePath = path.toString();
            String response = authenticationService.candidature(username, firstname,telephone, sexe, address, birthdate, birthplace, cnicardnumber, filePath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors du traitement de la candidature.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
