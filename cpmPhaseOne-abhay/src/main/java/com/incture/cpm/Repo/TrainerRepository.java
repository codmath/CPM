package com.incture.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, String> {
    
}
