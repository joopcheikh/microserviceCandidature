package com.registration.registration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.registration.repository.EmailSenderRepository;
import com.registration.registration.service.EmailSenderService;


@RestController
@RequestMapping("/email")
public class emailSenderController {

    @Autowired
    private EmailSenderService emailService;

    @Autowired
    private EmailSenderRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send")
    public ResponseEntity<Object> sendEmail(@RequestBody EmailRequest emailRequest) {
        if (emailService.emailExists(emailRequest.getTo())) {
            String validationCode = emailService.generateValidationCode();
            emailService.storeValidationCode(emailRequest.getTo(), validationCode);
    
            String bodyWithCode = emailRequest.getBody() + "\nVotre code de validation est: " + validationCode;
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), bodyWithCode);
    
            // Return JSON response
            return ResponseEntity.ok().body(new ApiResponse("success", "Email envoye avec success!"));
        }
        // Return JSON response for existing email
        return ResponseEntity.badRequest().body(new ApiResponse("error", "l'email exist deja!"));
    }

    // Create a simple class for API responses
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

    public class TokenResponse {
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

    @PostMapping("/validate")
    public ResponseEntity<TokenResponse> validateCode(@RequestBody ValidationRequest validationRequest) {
        boolean isValid = emailService.validateCode(validationRequest.getEmail(), validationRequest.getCode());
        
        if (isValid) {
            String token = emailService.generateToken(validationRequest.getEmail());
            TokenResponse response = new TokenResponse(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean isValidToken = emailService.validateToken(request.getEmail(), request.getToken());
        if (isValidToken) {
            String encryptedPassword = passwordEncoder.encode(request.getPassword());
            userRepository.updatePassword(request.getEmail(), encryptedPassword);
            emailService.removeToken(request.getEmail());
    
            ApiResponse response = new ApiResponse("success", "success");
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse("error", "error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public static class ChangePasswordRequest {
        private String email;
        private String token;
        private String password;

        // Getters et Setters
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
