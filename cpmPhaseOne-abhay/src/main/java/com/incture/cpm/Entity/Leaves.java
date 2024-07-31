package com.incture.cpm.Entity;

import java.sql.Blob;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Leaves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    private Long talentId;
    private String talentName;
    private String date;
    private String startDate;
    private String endDate;
    private String approvalStatus;
    private String approverName;

    private String subject;
    private String description;
    private String reasonForReject;

    private Blob reasonFile;
}
