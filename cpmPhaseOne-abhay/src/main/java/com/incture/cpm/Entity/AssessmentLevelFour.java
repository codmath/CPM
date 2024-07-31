package com.incture.cpm.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class AssessmentLevelFour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelFourId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;
    
    private double involved;
    private double teamPlayer;
    private double willingToCreate;
    private double tenacity;
    private double valueSystem;

    private double totalScore; // total of level 4

    public void updateTotalScore() {
        this.totalScore = this.involved + this.teamPlayer + this.willingToCreate + this.tenacity + this.valueSystem;
    }
}
