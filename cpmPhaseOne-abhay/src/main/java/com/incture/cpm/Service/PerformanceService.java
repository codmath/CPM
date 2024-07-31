package com.incture.cpm.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Entity.Performance;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.TalentAssessment;
import com.incture.cpm.Repo.AttendanceRepo;
import com.incture.cpm.Repo.EmployeewiseAssignmentRepo;
import com.incture.cpm.Repo.PerformanceRepo;
import com.incture.cpm.Repo.TalentAssessmentRepository;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class PerformanceService {
    @Autowired
    PerformanceRepo performanceRepo;

    @Autowired
    TalentRepository talentRepository;

    @Autowired
    EmployeewiseAssignmentRepo employeewiseAssignmentRepo;

    @Autowired
    TalentAssessmentRepository talentAssessmentRepo;

    @Autowired
    AttendanceRepo attendanceRepo;

    public List<Performance> getAllPerformances() {
        return performanceRepo.findAll();
    }

    public Performance getPerformanceById(Long talentId) {
        return performanceRepo.findById(talentId).get();
    }

    // to be removed latern on
    public String updatePerformance(Performance performance) {
        Talent existingTalent = talentRepository.findById(performance.getTalentId())
                .orElseThrow(() -> new IllegalArgumentException("Talent not found"));

        performanceRepo.findById(performance.getTalentId())
                .orElseThrow(() -> new IllegalArgumentException("Performance not found"));

        performance.setTalentName(existingTalent.getTalentName());
        performance.setEkYear(existingTalent.getEkYear());
        performance.setTalentSkills(existingTalent.getTalentSkills());
        performanceRepo.save(performance);
        updateAssignmentScore(existingTalent.getEmail());
        updateAssessmentScore(performance.getTalentId());
        updateAttendanceScore(performance.getTalentId());
        return "Performance updated successfully";
    }

    public String addPerformanceWithTalent(Talent talent) {
        Performance performance = new Performance();

        performance.setTalentId(talent.getTalentId());
        performance.setTalentName(talent.getTalentName());
        performance.setEkYear(talent.getEkYear());
        performance.setTalentSkills(talent.getTalentSkills());
        // History history = new History();
        // history.setEntityId(String.valueOf(performance.getPerformanceId()));
        // history.setEntityType("Performance");
        // history.setLogEntry("Performance entity created");
        // history.setTimestamp(new Date());
        // history.setUserName(authenticatedUser);

        // performance.getEditHistory().add(history);
        performanceRepo.save(performance);

        return "Performance created successfully";
    }

    public String editTalentDetails(Talent talent) {
        Performance existingPerformance = performanceRepo.findById(talent.getTalentId())
                .orElseThrow(() -> new IllegalStateException("Performance not found for given talent"));

        existingPerformance.setTalentName(talent.getTalentName());
        existingPerformance.setEkYear(talent.getEkYear());
        existingPerformance.setTalentSkills(talent.getTalentSkills());

        // History history = new History();
        // history.setEntityId(String.valueOf(existingPerformance.getPerformanceId()));
        // history.setEntityType("Performance");
        // history.setLogEntry("Talent details updated");
        // history.setTimestamp(new Date());
        // history.setUserName(authenticatedUser);

        // existingPerformance.getEditHistory().add(history);
        performanceRepo.save(existingPerformance);
        return "Performance updated successfully";
    }

    public void updateFeedback(Performance performance) {
        Performance existingPerformance = performanceRepo.findById(performance.getTalentId())
                .orElseThrow(() -> new IllegalStateException("Performance not found for given id"));

        existingPerformance.setAssessmentScore(performance.getAssessmentScore());
        existingPerformance.setAssignmentScore(performance.getAssignmentScore());
        existingPerformance.setAverageAttendance(performance.getAverageAttendance());

        existingPerformance.setPunctuality(performance.getPunctuality());
        existingPerformance.setTechnicalProficiency(performance.getTechnicalProficiency());
        existingPerformance.setProactiveness(performance.getProactiveness());
        existingPerformance.setTimeliness(performance.getTimeliness());
        // History history = new History();
        // history.setEntityId(String.valueOf(existingPerformance.getPerformanceId()));
        // history.setEntityType("Performance");
        // history.setLogEntry("Feedback edited");
        // history.setTimestamp(new Date());
        // history.setUserName(authenticatedUser);

        // existingPerformance.getEditHistory().add(history);


        performanceRepo.save(existingPerformance);
    }

    public String updateAssignmentScore(String email) {
        try {
            Talent talent = talentRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Could not find talent"));
            List<EmployeewiseAssignment> assignmentList = employeewiseAssignmentRepo.findByEmployeeEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("No record found for talent"));
            double sum = 0.0;
            int n = 0;
            for (EmployeewiseAssignment assignment : assignmentList) {
                sum += assignment.getEmployeeAssignmentScore();
                n += 1;
            }
            Performance performance = performanceRepo.findById(talent.getTalentId()).get();
            performance.setAssignmentScore(sum / n);
            performanceRepo.save(performance);
            return "Assignment score updated successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public String updateAssessmentScore(Long talentId) {
        try {
            List<TalentAssessment> talentAssessmentList = talentAssessmentRepo.findAllByTalentId(talentId)
                    .orElseThrow(() -> new IllegalArgumentException("Could not find talent"));
            double sum = 0.0;
            int n = 0;
            for (TalentAssessment talentAssessment : talentAssessmentList) {
                sum += Collections.max(talentAssessment.getScores());
                n += 1;
            }
            Performance performance = performanceRepo.findById(talentId).get();
            performance.setAssessmentScore(sum / n);
            performanceRepo.save(performance);
            return "Assessment score updated successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public String updateAttendanceScore(Long talentId) {
        try {
            List<Attendance> attendanceList = attendanceRepo.findByTalentId(talentId)
                    .orElseThrow(() -> new IllegalArgumentException("Could not find Attendane"));
            int absentDays = 0, presentDays = 0;
            for (Attendance attendance : attendanceList) {
                if ("Present".equalsIgnoreCase(attendance.getStatus())) {
                    presentDays++;
                } else if ("Absent".equalsIgnoreCase(attendance.getStatus())
                        || "On Leave".equalsIgnoreCase(attendance.getStatus())) {
                    absentDays++;
                }
            }
            Performance performance = performanceRepo.findById(talentId).get();
            performance.setAverageAttendance((presentDays * 100) / (double) (presentDays + absentDays));
            performanceRepo.save(performance);
            return "Assignment score updated successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public void deletePerformance(Performance performance) {
        Optional<Performance> existingPerformance = performanceRepo.findById(performance.getTalentId());
        if (existingPerformance.isEmpty())
            throw new IllegalArgumentException("Performance with ID " + performance.getTalentId() + " does not exist.");

        performanceRepo.delete(performance);
    }

    public void deletePerformance(Long talentId) {
        Optional<Performance> existingPerformance = performanceRepo.findById(talentId);
        if (existingPerformance.isEmpty())
            throw new IllegalArgumentException("Performance with ID " + talentId + " does not exist.");

        performanceRepo.deleteById(talentId);
    }

    public String addPerformanceByList(List<Performance> performanceList) {
        for (Performance performance : performanceList) {
            try {
                updatePerformance(performance);
            } catch (IllegalArgumentException e) {
                return "Error adding performance: " + e.getMessage();
            }
        }
        return "All performances saved successfully";
    }



    
    // @Transactional
    // @Scheduled(cron = "0 0 0 L * ?") // month end schedule
    // //@Scheduled(fixedDelay = 120000) // 120000 milliseconds = 2 minutes
    // public void saveFeedbackAndResetPerformance() {
    //     System.out.println("Scheduled task started");   
    //     List<Performance> performances = performanceRepo.findAll();

    //     for (Performance performance : performances) {
    //         History history = new History();
    //         history.setEntityId(String.valueOf(performance.getPerformanceId()));
    //         history.setEntityType("Performance");

    //         Map<String, Object> logEntryMap = new HashMap<>();
    //         logEntryMap.put("punctuality", performance.getPunctuality());
    //         logEntryMap.put("technicalProficiency", performance.getTechnicalProficiency());
    //         logEntryMap.put("proactiveness", performance.getProactiveness());
    //         logEntryMap.put("timeliness", performance.getTimeliness());

    //         try {
    //             String logEntryJson = objectMapper.writeValueAsString(logEntryMap);
    //             history.setLogEntry(logEntryJson);
    //         } catch (JsonProcessingException e) {
    //             throw new RuntimeException("Error converting log entry to JSON", e);
    //         }

    //         history.setTimestamp(new Date());
    //         history.setUserName("System Auto Save");
    //         performance.getMonthlyHistory().add(history);

    //         // Reset performance feedback values
    //         performance.setPunctuality(0);
    //         performance.setTechnicalProficiency(0);
    //         performance.setProactiveness(0);
    //         performance.setTimeliness(0);
    //         performanceRepo.save(performance);
    //     }
    // }

    // @Transactional
    // @Scheduled(cron = "0 0 0 L * ?") // month end scheldule
    // //@Scheduled(fixedDelay = 120000) // 120000 milliseconds = 2 minutes
    // public void sendMonthlyEmails() {
    //     List<User> reportingManagers = userRepository.findByRole("Reporting Manager");
    //     for (User user : reportingManagers) {
    //         String email = user.getEmail();
    //         String subject = "Monthly Performance Report Reminder";
    //         String text = "Dear " + user.getTalentName() + ",\n\nPlease ensure that all monthly reports are completed.\n\nBest regards,\nIncture";
    //         emailService.sendSimpleEmail(email, subject, text);
    //     }
    // }
}
