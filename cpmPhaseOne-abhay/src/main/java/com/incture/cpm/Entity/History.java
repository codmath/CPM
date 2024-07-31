package com.incture.cpm.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "History")
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entityId; // The ID of the related entity

    @Column(nullable = false)
    private String entityType; // The type of the related entity (e.g., "UnauthorizedUser", "Attendance")

    @Column(nullable = false, columnDefinition = "TEXT")
    private String logEntry;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp = new Date();

    @Column(nullable = false)
    private String userName; // Person responsible for the change
}
