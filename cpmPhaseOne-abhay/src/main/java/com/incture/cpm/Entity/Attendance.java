package com.incture.cpm.Entity;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor

public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private Long talentId;
    private String talentName;
    private String talentCategory;
    private String officeLocation;
    private String ekYear;
    private String status; // Present, Absent, On Leave, Holiday
    //private String holidayName;

    private String date;
    private String checkin;
    private String checkout;

    public String totalHours;

    @PrePersist
    @PreUpdate
    private void updateTotalHours() {
        calculateTotalHours();
    }

    private void calculateTotalHours() {
        if (checkin == null || checkout == null) {
            totalHours = "00:00";
            return;
        }
        LocalTime checkinTime = LocalTime.parse(checkin, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime checkoutTime = LocalTime.parse(checkout, DateTimeFormatter.ofPattern("HH:mm:ss"));

        Duration duration = Duration.between(checkinTime, checkoutTime);
        long hours = duration.toHours();
        long minutes = (duration.toMinutes() % 60); 
    
        totalHours = "%02d:%02d".formatted(hours, minutes);
    }
}
