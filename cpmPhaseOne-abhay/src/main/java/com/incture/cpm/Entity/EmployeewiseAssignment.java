package com.incture.cpm.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmployeewiseAssignment {
    @Id
    private int employeeAssignmentId;
    @Column
    private String employeeEmail;
    @Column
    private  String assignmentWeek;
    @Column
    private String assignmentName;
    @Column
    private String assignmentTechnology;
    @Column
    private String assignmentDuedate;
    @Column
    private int employeeAssignmentScore;
    @Column
    private String assignmentFileName;
    @Column
    private String assignmentStatus;
    @Column
    private String assignmentFileUrl;
    @Column
    private String employeeAssignmentFileUrl;
    @Column
    private int maxmarks;
    @Column
    private String employeeAssignmentFeedback;
    @Column
    private String evaluatorName;

}
