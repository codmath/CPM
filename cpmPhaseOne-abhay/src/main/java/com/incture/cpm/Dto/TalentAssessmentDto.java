package com.incture.cpm.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TalentAssessmentDto {
    private Long talentId;
    private Long assessmentId;
    private String assessmentType;
    private String assessmentSkill;
    private String location;
    private double score;
    private String assessmentDate;
}
