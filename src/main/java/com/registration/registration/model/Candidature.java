package com.registration.registration.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "candidature")
public class Candidature {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String firstname;

    private String telephone;

    private String sexe;

    private String adress;

    private Date birthdate;

    private String birthplace;

    private String cnicardnumber;

    private String filePath;

    private String password;

    @OneToOne
    private User user;

    @OneToMany
    private List<Concours> concours;

}
