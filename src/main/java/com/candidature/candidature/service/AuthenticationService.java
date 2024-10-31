package com.candidature.candidature.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.Candidature;
import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.CandidatureRepository;
import com.candidature.candidature.repository.UserRepository;

@Service
public class AuthenticationService {
    String smtpUsername = "gats.gatsmapping@gmail.com";

    private final CandidatureRepository candidatureRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            UserRepository userRepository,
            CandidatureRepository candidatureRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.candidatureRepository = candidatureRepository;
    }

    // Méthode pour traiter une candidature
    public AuthenticationResponse candidature(
        String lastname,
        String firstname,
        String telephone,
        String sexe,
        String address,
        Date birthdate,
        String birthplace,
        List<String> filePaths, // Liste des chemins de fichiers
        String concours,
        User user
    ) throws IllegalArgumentException {
        
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
        candidat.setFilePath(filePaths); // Stocker les chemins de fichiers
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

        // Réponse immédiate avant l'envoi de l'e-mail
        AuthenticationResponse response = new AuthenticationResponse(token, user.getRole().name(), "");

        // Envoi de l'email de façon asynchrone
        sendEmail(user.getEmail(), "CONFIRMATION RECEPTION CANDIDATURE", 
            "Bonjour, " + user.getLastname() + ", votre candidature au concours de " + concours + " a bien été reçue. Elle sera examinée et nous vous reviendrons.");

        return response; // Réponse au frontend sans attendre l'envoi d'email
    }

    // Méthode pour envoyer l'e-mail de façon asynchrone
    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(smtpUsername);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            System.out.println("Email envoyé avec succès !");
        } catch (Exception e) {
            System.out.println("Échec de l'envoi de l'email : " + e.getMessage());
            // Optionnel : journaliser l'erreur ou prendre une autre action
        }
    }
}
