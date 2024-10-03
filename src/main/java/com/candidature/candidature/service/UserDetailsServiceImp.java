package com.candidature.candidature.service;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.candidature.candidature.model.User;
import com.candidature.candidature.repository.UserRepository;

/**
 * @author cheikh diop
 *
 * Service d'implémentation de UserDetailsService. Cette classe est responsable de
 * charger les détails d'un utilisateur à partir de la base de données lors de l'authentification.
 * Elle est utilisée par Spring Security pour vérifier les informations d'identification de l'utilisateur.
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    /*
     * Lorsque l'utilisateur tente de se connecter, Spring Security utilise
     * le UserDetailsService pour récupérer un objet UserDetails qui contient
     * les informations de l'utilisateur, comme le nom d'utilisateur,
     * le mot de passe et les rôles. Cela permet de vérifier si l'utilisateur
     * existe et si ses informations sont correctes.
     */
    private final UserRepository userRepository;

    /**
     * Constructeur pour initialiser le UserDetailsService avec le repository d'utilisateurs.
     *
     * @param repository Le repository d'utilisateurs à utiliser pour les opérations de base de données.
     */
    public UserDetailsServiceImp(UserRepository repository) {
        this.userRepository = repository;
    }

    /**
     * Charge un utilisateur à partir de la base de données en utilisant son adresse email.
     *
     * @param username L'adresse email de l'utilisateur à charger.
     * @return Un objet UserDetails contenant les informations de l'utilisateur.
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé dans la base de données.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Créez une liste des autorités en fonction du rôle
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(user.getRole())));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
