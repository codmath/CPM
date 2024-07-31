package com.incture.cpm.Entity;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class InterviewerScheduling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;
    @OneToOne
    @JoinColumn(name = "college_id", referencedColumnName = "collegeId")
    private CollegeTPO collegeTPO;
    private String pptDate;
    private String assessmentDate;
    private String designDate;
    private String interviewDate;
    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private Interviewer interviewer;

}
