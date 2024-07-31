package com.incture.cpm.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter

public class Regularize {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regularizeId;
    
    private String talentName;
    private String checkin;
    private String checkout;
    private String approvalManager;
    private String approvalStatus;
    private String attendanceDate;
    private String regularizeDate;
    private Long talentId;
    
    @Column(nullable = true)
    private String reason;

    private String extraTime; // only for reason = WFH
    private String reasonForReject;
}
