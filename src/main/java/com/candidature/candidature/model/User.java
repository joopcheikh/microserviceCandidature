package com.candidature.candidature.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author cheikh diop
 *
 * Représente un utilisateur du système.
 *
 * Cette classe implémente l'interface UserDetails pour intégrer les
 * fonctionnalités de sécurité de Spring Security. Elle contient des
 * informations de l'utilisateur telles que le nom, le prénom, l'email,
 * le mot de passe, le rôle et une liste de candidatures associées.
 *
 * Les utilisateurs peuvent avoir différents rôles (ADMIN, USER, etc.)
 * et peuvent soumettre plusieurs candidatures.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstname;

    private Boolean have_postuled = false;

    private String type_candidat; // Type de candidat (ex: étudiant, professionnel, etc.)

    private String lastname;

    @Column(unique = true)
    private String email; // Adresse email unique pour chaque utilisateur

    private String password; // Mot de passe de l'utilisateur

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER; // Rôle de l'utilisateur, par défaut USER

    @OneToMany(mappedBy = "user")
    @JsonIgnore // Ignore cette propriété lors de la sérialisation JSON
    private List<Candidature> candidatures; // Liste des candidatures associées à l'utilisateur

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retourne les autorités (rôles) de l'utilisateur.
        // À implémenter pour retourner les rôles correspondants.
        return null;
    }

    @Override
    public String getPassword() {
        // Retourne le mot de passe de l'utilisateur.
        return password;
    }

    @Override
    public String getUsername() {
        // Retourne l'email de l'utilisateur comme identifiant.
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Vérifie si le compte de l'utilisateur n'est pas expiré.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Vérifie si le compte de l'utilisateur n'est pas verrouillé.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Vérifie si les identifiants (mot de passe) de l'utilisateur n'ont pas expiré.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Vérifie si le compte de l'utilisateur est activé.
        return true;
    }
}
