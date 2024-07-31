package com.incture.cpm.Entity;


import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

public class Performance {
    @Id
    @Column(name = "talent_id")
    private Long talentId;

    private String talentName;
    private String ekYear;
    private String talentSkills;

    private double assignmentScore;
    private double averageAttendance;
    private double assessmentScore;
    
    private int punctuality;
    private int technicalProficiency;
    private int proactiveness;
    private int timeliness;

    public void setAssessmentScore(double assessmentScore) {
        this.assessmentScore = roundToTwoDecimalPlaces(assessmentScore);
    }

    // Utility method to round to two decimal places
    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
