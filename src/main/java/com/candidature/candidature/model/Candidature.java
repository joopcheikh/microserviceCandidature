package com.candidature.candidature.model;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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

    private String lastname;

    private String firstname;

    private String telephone;

    private String sexe;

    private String adress;

    private Date birthdate;

    private String birthplace;

    private List<String> filePath; 
    
    // getters et setters
    public List<String> getFilePath() {
        return filePath;
    }

    public void setFilePath(List<String> filePath) {
        this.filePath = filePath;
    }

    private Status status = Status.WAITING;

    @Column(nullable = false)
    private String concours;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}
