package com.registration.registration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.registration.registration.model.User;
import com.registration.registration.repository.UserRepository;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User userRigistry) {
        User user = new User();
        user.setFirstname(userRigistry.getFirstname());
        user.setLastname(userRigistry.getLastname());
        user.setEmail(userRigistry.getEmail());
        user.setPassword(passwordEncoder.encode(userRigistry.getPassword()));
        user.setRole(userRigistry.getRole());

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse login(User authUser){
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
}
