package com.incture.cpm.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="interviewerList")
public class Interviewer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "interviewer_id", nullable = false, unique = true, updatable = false)
    private String interviewerId;
    private String inctureId;
    private String interviewerName;
    private String grade;
    private String techRole;
    private String techProficiency;
    private String location;
    private String region;
    private String email;
    private String mobileNumber;
    private List<String> prevBatches;
    private String workExperience;
    @JsonIgnore
    @OneToMany(mappedBy = "interviewer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewerScheduling> scheduleDetails = new ArrayList<>();
}
