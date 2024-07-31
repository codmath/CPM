package com.incture.cpm.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academicinterns")
public class AcademicInterns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "skill")
    private String meetingTitle;
    @Column(name = "meeting_duration")
    private String meetingDuration;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "date")
    private String date;
    @Column(name = "in_meeting_duration")
    private String inMeetingDuration;
    @Column(name = "email")
    private String email;
    @Column(name = "role")
    private String role;
    private String status; // Present, Absent
    // Constructors, getters, and setters
    public void setDurationMinutes(long minutes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDurationMinutes'");
    }
}