package com.incture.cpm.Entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TalentAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localKey;

    private Long talentId;
    private Long assessmentId;
    private String assessmentType;
    private String assessmentSkill;
    private String location;
    private List<Double> scores;
    private int attempts;
    @Column(columnDefinition = "TEXT")
    private String comments;
    private String assessmentDate;

}
