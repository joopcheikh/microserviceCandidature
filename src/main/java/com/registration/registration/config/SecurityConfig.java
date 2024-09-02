package com.registration.registration.config;

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

import com.registration.registration.filter.JwtAuthenticationFilter;
import com.registration.registration.service.UserDetailsServiceImp;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;



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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Le CSRF est désactivé ici pour permettre une configuration plus simple dans cet exemple
                .authorizeHttpRequests(
                        req -> req
                                .requestMatchers(
                                        "/login/**",
                                        "/register/**",
                                        "/swagger-ui/**",
                                        "/v3/api-docs",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**"
                                        )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .userDetailsService(userDetailsServiceImp)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                /*
                 * Cela permet à votre filtre JWT de vérifier la validité du jeton JWT dans chaque requête avant que Spring Security
                 * ne gère l'authentification de l'utilisateur.
                 */
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }

    /*
     * BCryptPasswordEncoder permet de coder et décoder les mots de passe en
     * utilisant l'algorithme de hachage BCrypt
     *
     * On va crypter les informations d'authentification avant de les sauvegarder
     * en base de données
     *
     *  L'encodage (ou hachage) du mot de passe le rend irréversible, ce qui
     *  signifie qu'il est difficile pour un attaquant d'obtenir le mot de
     *  passe réel même s'il accède à la base de données.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /* AuthenticationManager est responsable de la validation des informations
     * d'identification fournies par un utilisateur lors de la tentative de connexion.
     *
     * et le fait de le définir en tant que bean permet à Spring d'injecter
     * cette dépendance dans d'autres composants qui nécessitent des fonctionnalités
     * d'authentification, comme les filtres de sécurité.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // allow all origins to access our service
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                        .allowedHeaders("*");
            }
        };
    }
}



