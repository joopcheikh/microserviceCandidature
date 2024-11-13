package com.candidature.candidature.service;

import java.util.List;

import com.candidature.candidature.model.Candidature;

/**
 * @author cheikh diop
 *
 * Interface `CandidatureServices` qui définit les méthodes relatives à la gestion
 * des candidatures dans le système.
 */
public interface CandidatureServices {

    /**
     * Récupère la liste de toutes les candidatures.
     *
     * @return Une liste de toutes les candidatures présentes dans le système.
     */
    public List<Candidature> listerCandidature();
}
