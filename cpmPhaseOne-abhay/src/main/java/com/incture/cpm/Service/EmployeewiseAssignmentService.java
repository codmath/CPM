package com.incture.cpm.Service;

import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Repo.EmployeewiseAssignmentRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.incture.cpm.Repo.TalentRepository;
import java.util.List;
import java.util.Optional;
import com.incture.cpm.Entity.Talent;


@Service
public class EmployeewiseAssignmentService {
    @Autowired
    TalentRepository talentRepository;
    @Autowired
    private EmployeewiseAssignmentRepo employeewiseAssignmentRepo;
    @Autowired
    private EmailSenderService emailSenderService;

    public List<EmployeewiseAssignment> getAllEmployeeAssignments() {
        return employeewiseAssignmentRepo.findAll();
    }

    public EmployeewiseAssignment getEmployeeAssignmentById(int id) {
        return employeewiseAssignmentRepo.findById(id).orElse(null);
    }




    public EmployeewiseAssignment updateEmployeeAssignment(int id, EmployeewiseAssignment updatedEmployeeAssignment, boolean sendFeedback) {
        // Retrieve the existing assignment entity from the repository based on the ID
        Optional<EmployeewiseAssignment> optionalExistingEmployeeAssignment = employeewiseAssignmentRepo.findById(id);
        if (optionalExistingEmployeeAssignment.isPresent()) {
            // If the existing assignment entity is found, get it from the optional
            EmployeewiseAssignment existingEmployeeAssignment = optionalExistingEmployeeAssignment.get();

            // Check if there's a change in the score
            int oldScore = existingEmployeeAssignment.getEmployeeAssignmentScore();
            int newScore = updatedEmployeeAssignment.getEmployeeAssignmentScore();
            boolean scoreChanged = oldScore != newScore;

            // Update all properties of the existing assignment entity with the properties of the updated assignment entity
            existingEmployeeAssignment.setEmployeeEmail(updatedEmployeeAssignment.getEmployeeEmail());
            existingEmployeeAssignment.setAssignmentWeek(updatedEmployeeAssignment.getAssignmentWeek());
            existingEmployeeAssignment.setAssignmentName(updatedEmployeeAssignment.getAssignmentName());
            existingEmployeeAssignment.setAssignmentTechnology(updatedEmployeeAssignment.getAssignmentTechnology());
            existingEmployeeAssignment.setAssignmentDuedate(updatedEmployeeAssignment.getAssignmentDuedate());
            existingEmployeeAssignment.setEmployeeAssignmentScore(newScore);
            existingEmployeeAssignment.setAssignmentFileName(updatedEmployeeAssignment.getAssignmentFileName());
            existingEmployeeAssignment.setAssignmentStatus(updatedEmployeeAssignment.getAssignmentStatus());
            existingEmployeeAssignment.setAssignmentFileUrl(updatedEmployeeAssignment.getAssignmentFileUrl());
            existingEmployeeAssignment.setEmployeeAssignmentFileUrl(updatedEmployeeAssignment.getEmployeeAssignmentFileUrl());
            existingEmployeeAssignment.setEmployeeAssignmentFeedback(updatedEmployeeAssignment.getEmployeeAssignmentFeedback());

            // Save the updated assignment entity back to the repository
            EmployeewiseAssignment savedAssignment = employeewiseAssignmentRepo.save(existingEmployeeAssignment);

            // If sendFeedback is true and there's a change in score, send an email notification with score and feedback
            if (sendFeedback && scoreChanged) {
                // Retrieve the talent based on the name from the talent table
                Talent talent = talentRepository.findByEmail(savedAssignment.getEmployeeEmail()).orElseThrow(() -> new IllegalStateException("Could not find talent"));
                if (talent != null) {
                    // Compose email body with the new score and feedback
                    String emailBody = "Dear " + talent.getTalentName() + ",\n\n"
                            + "Your assignment score has been updated.\n\n"
                            + "New Score: " + newScore + "\n"
                            + "Feedback: " + savedAssignment.getEmployeeAssignmentFeedback() + "\n\n"
                            + "Best regards,\n"
                            + "Incture";

                    // Send email to talent
                    emailSenderService.sendSimpleEmail(talent.getEmail(), "Assignment Score Updated", emailBody);
                } else {
                    System.out.println("Talent with name " + savedAssignment.getEmployeeEmail() + " not found.");
                }
            }
            // If sendFeedback is false, send only the score
            else if (!sendFeedback && scoreChanged) {
                // Retrieve the talent based on the name from the talent table
                Talent talent = talentRepository.findByEmail(savedAssignment.getEmployeeEmail()).orElseThrow(() -> new IllegalStateException("Could not find talent"));
                if (talent != null) {
                    // Compose email body with only the new score
                    String emailBody = "Dear " + talent.getTalentName() + ",\n\n"
                            + "Your assignment score has been updated.\n\n"
                            + "Feedback: " + savedAssignment.getEmployeeAssignmentFeedback() + "\n\n"
                            + "Best regards,\n"
                            + "Incture";

                    // Send email to talent
                    emailSenderService.sendSimpleEmail(talent.getEmail(), "Assignment Score Updated", emailBody);
                } else {
                    System.out.println("Talent with email " + savedAssignment.getEmployeeEmail() + " not found.");
                }
            }

            return savedAssignment;
        }
        return null; // or handle the absence of the entity in another way
    }





    public void deleteEmployeeAssignmentById(int id) {
        employeewiseAssignmentRepo.deleteById(id);
    }
}

