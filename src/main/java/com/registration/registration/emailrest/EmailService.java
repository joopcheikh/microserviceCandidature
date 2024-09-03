package com.registration.registration.emailrest;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserResetRepository userRepository; // Injection du repository

    private ConcurrentHashMap<String, CodeEntry> validationCodes = new ConcurrentHashMap<>();
    private final long expirationTime = TimeUnit.MINUTES.toMillis(15); // 15 minutes

    // Classe interne pour gérer les codes et leur timestamp
    private static class CodeEntry {
        String code;
        long timestamp;

        CodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }

    private ConcurrentHashMap<String, String> tokens = new ConcurrentHashMap<>();

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString(); // Générer un token unique
        tokens.put(email, token); // Associer le token à l'email
        return token;
    }
    public void removeToken(String email) {
        tokens.remove(email);
        validationCodes.remove(email);
    }
    
    public boolean validateToken(String email, String token) {
        String storedToken = tokens.get(email);
        return storedToken != null && storedToken.equals(token);
    }

    public String generateValidationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void storeValidationCode(String email, String code) {
        // Stocker le code avec un timestamp
        long timestamp = System.currentTimeMillis();
        validationCodes.put(email, new CodeEntry(code, timestamp));
    }

    public boolean validateCode(String email, String code) {
        CodeEntry entry = validationCodes.get(email);

        // Vérifier si le code existe
        if (entry == null) {
            return false; // Code non trouvé
        }

        // Vérifier l'expiration
        if (System.currentTimeMillis() - entry.timestamp > expirationTime) {
            validationCodes.remove(email); // Supprimer le code expiré
            return false; // Code expiré
        }

        // Vérifier si le code correspond
        return entry.code.equals(code);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email); // Utiliser le repository pour vérifier l'existence de l'email
    }
}
