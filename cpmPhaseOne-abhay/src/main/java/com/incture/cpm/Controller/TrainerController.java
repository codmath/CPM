package com.incture.cpm.Controller;

import com.incture.cpm.Entity.*;
import com.incture.cpm.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/api/trainers")
@CrossOrigin("*")
public class TrainerController {
 
    @Autowired
    private TrainerService trainerService;
 
    @GetMapping("/getAll")
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }
 
    @GetMapping("/{trainerId}")
    public ResponseEntity<Trainer> getTrainerById(@PathVariable String trainerId) {
        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId);
        if (trainer.isPresent()) {
            return ResponseEntity.ok(trainer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
 
    @PostMapping("/create")
    public Trainer createTrainer(@RequestBody Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }
 
    @PutMapping("/update/{trainerId}")
    public ResponseEntity<Trainer> updateTrainer(@PathVariable String trainerId, @RequestBody Trainer trainerDetails) {
        try {
            Trainer updatedTrainer = trainerService.updateTrainer(trainerId, trainerDetails);
            return ResponseEntity.ok(updatedTrainer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
 
    @DeleteMapping("/delete/{trainerId}")
    public ResponseEntity<Void> deleteTrainer(@PathVariable String trainerId) {
        try {
            trainerService.deleteTrainer(trainerId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}