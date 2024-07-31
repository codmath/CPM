package com.incture.cpm.Entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidate {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;
 
    private String candidateName;
    // @ManyToOne
    private String candidateCollege;
    private String department;
    private int collegeId;

    // @Column(unique = true, nullable = false)
    private String email;
    private String phoneNumber;
    private String alternateNumber;
    private double tenthPercent;
    private double twelthPercent;
    // private String marksheetsSemwise;
    private String currentLocation;
    private String permanentAddress;
    private String panNumber;
    private String aadhaarNumber;
    private String fatherName;
    private String motherName;
    private String DOB;
    private double cgpaUndergrad;
    private double cgpaMasters;
    private String status = "interview pending";
    private String EkYear;
 
}
