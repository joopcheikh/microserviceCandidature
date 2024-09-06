package com.registration.registration.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registration.registration.model.Concours;
import com.registration.registration.repository.ConcoursRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConcoursServiceImpl implements ConcoursService {

    @Autowired
    private ConcoursRepository concoursRepository;
    
    @Override
    public Concours addConcours(Concours concours) {

        if(concours == null){
            log.error("concours is null");
            return null;
        }

        return concoursRepository.save(concours);
    }

    @Override
    public Concours changeConcours(Concours concours) {

        Optional<Concours> existingConcours = concoursRepository.findById(concours.getId());
        if (existingConcours.isPresent()) {
            Concours concoursToUpdate = existingConcours.get();
            concoursToUpdate.setNom(concours.getNom());
            concoursToUpdate.setDescription(concours.getDescription());
            return concoursRepository.save(concoursToUpdate);
        } else {
            log.error("Le concours n'est pas trouve");
            return null;
        }
    }

    @Override
    public Optional<Concours> findConcourById(Integer id) {
        if (id == null) {
            log.error("id is null");
        }

        Optional<Concours> concoursFinded = concoursRepository.findById(id);
        return concoursFinded;
    }

    @Override
    public void deleteConcour(Integer id) {
        if (id == null) {
            log.error("id is null");
        }

        concoursRepository.deleteById(id);
    }

    public List<Concours> getAllConcours() {
        return concoursRepository.findAll();
    }

}
