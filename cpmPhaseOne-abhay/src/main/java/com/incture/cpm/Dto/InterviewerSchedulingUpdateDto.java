package com.incture.cpm.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerSchedulingUpdateDto {
    private int scheduleId;
    private int collegeId;
    private String pptDate;
    private String assessmentDate;
    private String designDate;
    private String interviewDate;
    private String interviewerId;
}
