package com.incture.cpm.Service;

import com.incture.cpm.Entity.*;
import com.incture.cpm.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.Optional;
 
@Service
public class TrainerService {
 
    @Autowired
    private TrainerRepository trainerRepository;
 
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }
 
    public Optional<Trainer> getTrainerById(String trainerId) {
        return trainerRepository.findById(trainerId);
    }
 
    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }
 
    public Trainer updateTrainer(String trainerId, Trainer trainerDetails) {
        trainerRepository.findById(trainerId).orElseThrow(() -> new RuntimeException("Trainer not found"));
        trainerDetails.setTrainerId(trainerId);
        return trainerRepository.save(trainerDetails);
    }
 
    public void deleteTrainer(String trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> new RuntimeException("Trainer not found"));
        trainerRepository.delete(trainer);
    }
}