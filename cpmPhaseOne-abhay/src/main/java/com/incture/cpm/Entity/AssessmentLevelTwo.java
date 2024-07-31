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

public class AssessmentLevelTwo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long levelTwoId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;
    
    private double problemStatement;
    private double processWorkflow;
    private double useOfAlgorithms;
    private double contentTotal; // total of the above four scores

    private double techStacks;
    private double recommendedSolution;
    private double relevanceTotal; // total of the above two scores

    private double languageAndGrammar;
    private double logicalFlow;
    private double presentationTotal; // total of the above two scores

    private double totalScore; // total of level 2

    public void updateTotalScore() {
        this.contentTotal = this.problemStatement + this.processWorkflow + this.useOfAlgorithms;
        this.relevanceTotal = this.techStacks + this.recommendedSolution;
        this.presentationTotal = this.languageAndGrammar + this.logicalFlow;
        this.totalScore = this.contentTotal + this.relevanceTotal + this.presentationTotal;
    }
}
