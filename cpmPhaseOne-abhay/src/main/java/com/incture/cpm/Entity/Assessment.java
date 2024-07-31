package com.incture.cpm.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assessmentId;

    @Column(unique = true, nullable = false)
    private String email;
    private String candidateName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelOneId", referencedColumnName = "levelOneId")
    private AssessmentLevelOne assessmentLevelOne;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelTwoId", referencedColumnName = "levelTwoId")
    private AssessmentLevelTwo assessmentLevelTwo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelThreeId", referencedColumnName = "levelThreeId")
    private AssessmentLevelThree assessmentLevelThree;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelFourId", referencedColumnName = "levelFourId")
    private AssessmentLevelFour assessmentLevelFour;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelFiveId", referencedColumnName = "levelFiveId")
    private AssessmentLevelFive assessmentLevelFive;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "levelFinalId", referencedColumnName = "levelFinalId")
    private AssessmentLevelFinal assessmentLevelFinal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="college_id")
    private CollegeTPO college;

    private double totalScore;

    @PrePersist
    @PreUpdate
    public void updateTotalScore() {
        this.totalScore = (assessmentLevelOne != null ? assessmentLevelOne.getTotalScore() : 0)
                + (assessmentLevelTwo != null ? assessmentLevelTwo.getTotalScore() : 0)
                + (assessmentLevelThree != null ? assessmentLevelThree.getTotalScore() : 0)
                + (assessmentLevelFour != null ? assessmentLevelFour.getTotalScore() : 0)
                + (assessmentLevelFive != null ? assessmentLevelFive.getHrScore() : 0);
    }
}

// totalScore does't calculate the total score properly
