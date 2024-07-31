package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Repo.InkathonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InkathonService {

    @Autowired
    private InkathonRepository incathonRepository;

    public List<Inkathon> getAllIncathons() {
        return incathonRepository.findAll();
    }

    public Optional<Inkathon> getIncathonById(Long id) {
        return incathonRepository.findById(id);
    }

    public Inkathon createInkathon(String name,String description) {
        Inkathon inkathon=new Inkathon();
        inkathon.setTitle(name);
        inkathon.setDescription(description);
        inkathon.setCreationDate(LocalDate.now());
        return incathonRepository.save(inkathon);
    }

    public Inkathon updateIncathon(Long id, Inkathon updatedIncathon) {
        Optional<Inkathon> existingIncathonOptional = incathonRepository.findById(id);
        if (existingIncathonOptional.isPresent()) {
            Inkathon existingIncathon = existingIncathonOptional.get();
            existingIncathon.setTitle(updatedIncathon.getTitle());
            existingIncathon.setCreationDate(updatedIncathon.getCreationDate());
            existingIncathon.setDescription(updatedIncathon.getDescription());
            return incathonRepository.save(existingIncathon);
        } else {
            throw new IllegalArgumentException("Incathon not found with id: " + id);
        }
    }

    public void deleteIncathon(Long id) {
        incathonRepository.deleteById(id);
    }
}
