package com.registration.registration.service;

import com.registration.registration.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    /*
     Lorsque l'utilisateur tente de se connecter, Spring Security utilise
     le UserDetailsService pour récupérer un objet UserDetails qui contient
      les informations de l'utilisateur, comme le nom d'utilisateur,
      le mot de passe, et les rôles. Cela permet de vérifier si l'utilisateur
      existe et si ses informations sont correctes.
     */
    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * This method allow us to load user from database by this username
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
