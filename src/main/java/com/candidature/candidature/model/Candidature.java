package com.candidature.candidature.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * @author cheikh diop, sosthene
 *
 * La classe `Candidature` représente une candidature soumise par un utilisateur
 * pour un concours spécifique. Elle encapsule toutes les informations pertinentes
 * relatives à la candidature ainsi que la relation entre l'utilisateur et sa candidature.
 *
 * Attributs :
 * - id : Identifiant unique de la candidature (auto-généré).
 * - lastname : Nom de famille du candidat.
 * - firstname : Prénom du candidat.
 * - telephone : Numéro de téléphone du candidat.
 * - sexe : Sexe du candidat (peut être "Masculin", "Féminin", etc.).
 * - adress : Adresse physique du candidat.
 * - birthdate : Date de naissance du candidat.
 * - birthplace : Lieu de naissance du candidat.
 * - filePath : Liste des chemins d'accès aux fichiers joints à la candidature,
 *   représentant des documents que le candidat soumet (CV, lettres de motivation, etc.).
 * - status : État actuel de la candidature, défini par l'énumération `Status`
 *   (WAITING, ACCEPTED, REJECTED).
 * - concours : Nom du concours pour lequel la candidature est soumise.
 * - user : Référence à l'utilisateur qui a soumis la candidature, lié par une relation ManyToOne.
 *
 * Contraintes :
 * - La table `candidature` a une contrainte d'unicité sur la combinaison de `user_id` et `concours`,
 *   assurant qu'un utilisateur ne peut soumettre qu'une seule candidature pour un même concours.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "candidature",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "concours"})
        }
)
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom de famille du candidat.
     */
    private String lastname;

    /**
     * Prénom du candidat.
     */
    private String firstname;

    /**
     * Numéro de téléphone du candidat.
     */
    private String telephone;

    /**
     * Sexe du candidat. (ex: "Masculin", "Féminin")
     */
    private String sexe;

    /**
     * Adresse physique du candidat.
     */
    private String adress;

    /**
     * Date de naissance du candidat.
     */
    private Date birthdate;

    /**
     * Lieu de naissance du candidat.
     */
    private String birthplace;

    /**
     * Liste des chemins d'accès aux fichiers joints à la candidature,
     * tels que le CV ou la lettre de motivation.
     */
    private List<String> filePath;

    /**
     * État actuel de la candidature.
     * Défaut : WAITING (en attente).
     */
    private Status status = Status.WAITING;

    /**
     * Nom du concours pour lequel la candidature est soumise.
     */
    @Column(nullable = false)
    private String concours;

    /**
     * Référence à l'utilisateur qui a soumis la candidature.
     * Relation ManyToOne, garantissant qu'un utilisateur peut avoir plusieurs candidatures.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    // Getters et setters pour filePath
    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }
}
