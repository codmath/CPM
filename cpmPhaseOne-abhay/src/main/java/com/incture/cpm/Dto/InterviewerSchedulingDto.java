package com.incture.cpm.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerSchedulingDto {
    private int scheduleId;
    private String pptDate;
    private String assessmentDate;
    private String designDate;
    private String collegeName;
    private String location;
    private String region;
    private String interviewerName;
    private String interviewDate;
    private String grade;
}
