package com.incture.cpm.Dto;

import com.incture.cpm.Entity.InterviewerScheduling;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewerDto {
    private String interviewerId;
    private String inctureId;
    private String interviewerName;
    private String grade;
    private String techRole;
    private String techProficiency;
    private String location;
    private String region;
    private String workExperience;
    private  String email;
    private String mobileNumber;
    private List<String> prevBatches;
    private List<InterviewerScheduling> scheduleDetails;
}