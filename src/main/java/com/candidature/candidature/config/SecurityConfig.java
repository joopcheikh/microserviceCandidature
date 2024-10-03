package com.candidature.candidature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.candidature.candidature.filter.JwtAuthenticationFilter;
import com.candidature.candidature.service.UserDetailsServiceImp;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * @author cheikh diop
 *
 * Configuration de la sécurité de l'application.
 * Cette classe configure les règles d'authentification et d'autorisation
 * ainsi que le filtre d'authentification JWT.
 */
@Configuration
@EnableWebSecurity
@SecurityRequirement(name = "bearerAuth")
public class SecurityConfig {

    private UserDetailsServiceImp userDetailsServiceImp;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(
            UserDetailsServiceImp userDetailsServiceImp,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configure le filtre de sécurité pour gérer l'authentification et l'autorisation des requêtes.
     *
     * @param httpSecurity l'objet HttpSecurity pour configurer la sécurité web
     * @return SecurityFilterChain configuré
     * @throws Exception en cas d'erreur lors de la configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Désactive la protection CSRF
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers("/get-candidatures", "/files/**").hasAnyAuthority("ADMIN", "RECRUITER", "USER") // Autorisations pour les requêtes spécifiques
                                .requestMatchers("/candidature/delete/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/accept-candidature", "/reject-candidature").hasAuthority("RECRUITER") // Autorisations spécifiques pour les
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/email/send",
                                        "/email/validate",
                                        "/email/change-password").permitAll() // Permet l'accès sans authentification aux endpoints spécifiés.

                                .anyRequest()
                                .authenticated() // Tout autre accès nécessite une authentification
                )
                .userDetailsService(userDetailsServiceImp) // Utilise le service d'utilisateur pour l'authentification
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Utilise une politique sans état pour les sessions
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT avant le filtre d'authentification standard
                .build();
    }

    /**
     * Définit un encodeur de mot de passe basé sur BCrypt.
     *
     * @return PasswordEncoder pour le chiffrement des mots de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit un gestionnaire d'authentification.
     *
     * @param configuration la configuration d'authentification
     * @return AuthenticationManager pour gérer l'authentification
     * @throws Exception en cas d'erreur lors de la récupération du gestionnaire d'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
