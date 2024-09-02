package com.registration.registration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.service.EmailSenderService;

@RestController
@RequestMapping("/email")
public class EmailSenderController {

    @Autowired
    private EmailSenderService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getTo();
        if (emailService.emailExists(email)) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", "L'email existe déjà !"));
        }

        String validationCode = emailService.generateValidationCode();
        emailService.storeValidationCode(email, validationCode);

        String bodyWithCode = emailRequest.getBody() + "\nVotre code de validation est : " + validationCode;
        emailService.sendEmail(email, emailRequest.getSubject(), bodyWithCode);

        return ResponseEntity.ok(new ApiResponse("success", "Email envoyé avec succès !"));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenResponse> validateCode(@RequestBody ValidationRequest validationRequest) {
        boolean isValid = emailService.validateCode(validationRequest.getEmail(), validationRequest.getCode());
        if (isValid) {
            String token = emailService.generateToken(validationRequest.getEmail());
            return ResponseEntity.ok(new TokenResponse(token));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean isValidToken = emailService.validateToken(request.getEmail(), request.getToken());
        if (isValidToken) {
            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            emailService.updatePassword(request.getEmail(), encryptedPassword); // Assurez-vous que cette méthode est correcte dans le service
            emailService.removeToken(request.getEmail());
            return ResponseEntity.ok(new ApiResponse("success", "Mot de passe changé avec succès !"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "Token invalide !"));
    }

    public static class ApiResponse {
        private String status;
        private String message;

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class TokenResponse {
        private String token;

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class ChangePasswordRequest {
        private String email;
        private String token;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class EmailRequest {
        private String to;
        private String subject;
        private String body;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    public static class ValidationRequest {
        private String email;
        private String code;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
