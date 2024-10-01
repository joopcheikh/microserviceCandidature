package com.candidature.candidature.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.CandidatureRepository;
import com.candidature.candidature.repository.UserRepository;

import io.jsonwebtoken.io.IOException;

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

    // Methode pour les candidatures
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
        candidat.setFilePath(filePaths); // Stocker le tableau de chemins de fichiers
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

        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        System.out.println("User ID: " + user.getId());
        System.out.println("User Email: " + user.getEmail());
        System.out.println("User Role: " + (user.getRole() != null ? user.getRole().name() : "Role non défini"));


        String token = jwtService.generateToken(user);
        System.out.println("token: "+ token);
        AuthenticationResponse response = new AuthenticationResponse(token, user.getRole().name(), "");
        System.out.println("Response Token: " + response.getToken());
        System.out.println("Response Role: " + response.getRole());
        System.out.println("Response Error: " + response.getError());
        
        return response;
    }
}
          