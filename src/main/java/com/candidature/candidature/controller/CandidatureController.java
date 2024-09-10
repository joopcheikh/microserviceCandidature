package com.candidature.candidature.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.candidature.candidature.service.AuthenticationService;

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
            @RequestParam("address") String address,
            @RequestParam("birthdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate,
            @RequestParam("birthplace") String birthplace,
            @RequestParam("cnicardnumber") String cnicardnumber,
            @RequestParam("file") MultipartFile file,
            @RequestParam("concours") String concours
    ) {
        try {
            System.out.println("next to the problem");
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Aucun fichier sélectionné.");
            }
    
            Path directory = Paths.get("candidatures/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
    
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.contains("..")) {
                return ResponseEntity.badRequest().body("Nom de fichier invalide.");
            }
            
            String fileExtension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex); 
            }
            
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
    
            Path path = directory.resolve(uniqueFilename);
    
            try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Erreur lors de l'enregistrement du fichier.");
            }
    
            String filePath = path.toString();
            String response = authenticationService.candidature(username, firstname, telephone, sexe, address, birthdate, birthplace, cnicardnumber, filePath, concours);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors du traitement de la candidature.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
