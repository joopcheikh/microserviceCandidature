package com.registration.registration.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.registration.registration.emailrest.EmailService;
import com.registration.registration.model.Candidature;
import com.registration.registration.model.User;
import com.registration.registration.repository.CandidatureRepository;
import com.registration.registration.repository.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CandidatureRepository candidatureRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            CandidatureRepository candidatureRepository,
            AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.candidatureRepository = candidatureRepository;

    }

    public AuthenticationResponse register(User userRigistry) {
        User user = new User();
        user.setFirstname(userRigistry.getFirstname());
        user.setLastname(userRigistry.getLastname());
        user.setEmail(userRigistry.getEmail());
        user.setPassword(passwordEncoder.encode(userRigistry.getPassword()));

        String emailBody = "Bonjour votre compte a ete cree avec succes";
        emailService.sendEmail(user.getEmail(), "Alerte creation de compte gatsmapping", emailBody);

        // Return JSON response
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(User authUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUser.getEmail(),
                        authUser.getPassword()
                )
        );

        User user = userRepository.findUserByEmail(authUser.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    // Methode pour les candidatures
    public String candidature(String username, String firstname, String telephone, String sexe, String address, Date birthdate, String birthplace, String cnicardnumber, String filePath) throws IOException {
        // Vérifiez que le fichier est un PDF
        Path file = Paths.get(filePath);
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Le fichier doit être un PDF.");
        }
        System.out.println("i m here");
        // Vérifiez si l'utilisateur existe déjà
        //Optional<Candidature> existingCandidat = candidatureRepository.findCandidatureByUsername(username);
        Optional<Candidature> existingCandidat = candidatureRepository.findCandidatureByUsername(username);
        if (existingCandidat.isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec ce nom d'utilisateur existe déjà.");
        }
        System.out.println("next to the problem");
        // Enregistrez les informations de l'utilisateur
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
