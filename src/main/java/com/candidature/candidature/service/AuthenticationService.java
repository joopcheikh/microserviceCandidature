package com.candidature.candidature.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.repository.CandidatureRepository;
import com.candidature.candidature.repository.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class AuthenticationService {


    private final CandidatureRepository candidatureRepository;



    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            CandidatureRepository candidatureRepository,
            AuthenticationManager authenticationManager) {


        this.candidatureRepository = candidatureRepository;

    }



    // Methode pour les candidatures
    public String candidature(String username, String firstname, String telephone, String sexe, String address, Date birthdate, String birthplace, String cnicardnumber, String filePath, String concours) throws IOException {
        // Vérifiez que le fichier est un PDF
        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Le fichier doit être un PDF.");
        }
       
        Candidature candidat = new Candidature();
        candidat.setUsername(username);
        candidat.setFirstname(firstname);
        candidat.setTelephone(telephone);
        candidat.setSexe(sexe);
        candidat.setAdress(address);
        candidat.setBirthdate(birthdate);
        candidat.setBirthplace(birthplace);
        candidat.setCnicardnumber(cnicardnumber);
        candidat.setFilePath(filePath); // Stocker le chemin du fichier
        candidat.setConcours(concours);
        // user.setRole("ROLE_CANDIDATE"); // Décommentez si nécessaire
        // user.setPassword(""); // Définissez un mot de passe si nécessaire
        System.out.println("nexproblem");
        try {
            candidatureRepository.save(candidat);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur.", e);
        }

        return "Candidature reçue avec succès.";
    }
}