package com.registration.registration.service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.registration.registration.repository.EmailSenderRepository;

import jakarta.transaction.Transactional;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailSenderRepository emailSenderRepository;

    private ConcurrentHashMap<String, CodeEntry> validationCodes = new ConcurrentHashMap<>();
    private final long expirationTime = TimeUnit.MINUTES.toMillis(15);

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
        String token = UUID.randomUUID().toString();
        tokens.put(email, token);
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
        try {
            mailSender.send(message);
            System.out.println("Email envoyé à : " + to);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void storeValidationCode(String email, String code) {
        long timestamp = System.currentTimeMillis();
        validationCodes.put(email, new CodeEntry(code, timestamp));
    }

    public boolean validateCode(String email, String code) {
        CodeEntry entry = validationCodes.get(email);
        if (entry == null) return false;
        if (System.currentTimeMillis() - entry.timestamp > expirationTime) {
            validationCodes.remove(email);
            return false;
        }
        return entry.code.equals(code);
    }

    public boolean emailExists(String email) {
        return emailSenderRepository.existsByEmail(email);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        emailSenderRepository.updatePassword(email, newPassword);
    }
}
