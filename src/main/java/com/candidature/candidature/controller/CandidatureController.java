package com.candidature.candidature.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.UserRepository;
import com.candidature.candidature.service.AuthenticationResponse;
import com.candidature.candidature.service.AuthenticationService;
import com.candidature.candidature.service.CandidatureServiceImpl;
import com.candidature.candidature.service.UserService;

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
            @RequestParam("username") String username,
            @RequestParam("firstname") String firstname,
            @RequestParam("telephone") String telephone,
            @RequestParam("sexe") String sexe,
            @RequestParam("address") String address,
            @RequestParam("birthdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthdate,
            @RequestParam("birthplace") String birthplace,
            @RequestParam("cnicardnumber") String cnicardnumber,
            @RequestParam("file") MultipartFile file,
            @RequestParam("concours") String concours) {
        try {
            System.out.println("next to the problem");
            if (file == null || file.isEmpty()) {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setError("Aucun fichier sélectionné.");
                response.setToken(null);
                response.setRole(null);
                return ResponseEntity.badRequest().body(response);
            }

            Path directory = Paths.get("candidatures/");
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.contains("..")) {
                AuthenticationResponse response = new AuthenticationResponse();
                response.setError("Nom de fichier invalide.");
                response.setToken(null);
                response.setRole(null);
                return ResponseEntity.badRequest().body(response);
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
                AuthenticationResponse response = new AuthenticationResponse();
                response.setToken(null);
                response.setRole(null);
                response.setError("Erreur lors de l'enregistrement du fichier.");
                return ResponseEntity.badRequest().body(response);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findUserByEmail(email);
            System.out.println(user);
            String filePath = path.toString();
            AuthenticationResponse response = authenticationService.candidature(username, firstname, telephone, sexe,
                    address, birthdate, birthplace, cnicardnumber, filePath, concours, user);
            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(null);
            response.setRole(null);
            response.setError("Erreur lors du traitement de la candidature.");
            return ResponseEntity.badRequest().body(response);
        } catch (IllegalArgumentException e) {
            AuthenticationResponse response = new AuthenticationResponse();
            response.setToken(null);
            response.setRole(null);
            response.setError("Erreur lors du traitement de la candidature.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/candidature/delete/{id}")
    public ResponseEntity<String> deleteCandidature(@PathVariable("id") Integer candidatureId) {
        boolean isDeleted = candidatureServiceImp.deleteCandidatureById(candidatureId);

        if (isDeleted) {
            return ResponseEntity.ok("Candidature supprimée avec succès.");
        } else {
            return ResponseEntity.status(404).body("Candidature non trouvée.");
        }
    }
}
