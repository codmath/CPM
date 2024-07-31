package com.incture.cpm.Service;
import com.incture.cpm.Entity.Assignment;
import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Entity.Evaluator;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.AssignmentRepo;
import com.incture.cpm.Repo.EmployeewiseAssignmentRepo;
import com.incture.cpm.Repo.EvaluatorRepo;
import com.incture.cpm.Repo.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private TalentRepository talentRepository;
    @Autowired
    private EmployeewiseAssignmentRepo employeewiseAssignmentRepo;
 @Autowired
    private EvaluatorRepo evaluatorRepo;

    public Assignment createAssignment(Assignment assignment) {
        // Generate random assignment ID
          assignment.setAssignmentId(generateRandomInt());

        // Save the assignment
        Assignment savedAssignment = assignmentRepo.save(assignment);

        // Fetch talents based on assignedTo
        String assignedTo = assignment.getAssignedTo();
        String[] assignedToNames = assignedTo.split(",\\s*");
        int size_of_learners=assignedToNames.length;
        System.out.println(size_of_learners);
        List<Long> evaluatorsId=assignment.getEvaluatorId();
        int size_of_evaluators=evaluatorsId.size();
        System.out.println(size_of_evaluators);
        List<Evaluator> evaluators=evaluatorRepo.findAllById(evaluatorsId);
        List<String> evaluatorsEmail=evaluators.stream()
                .map(Evaluator::getEmail)
                .collect(Collectors.toList());
        System.out.println(evaluatorsEmail);
            int assignments_per_evaluator=size_of_learners/size_of_evaluators;
            int extra_assignements=size_of_learners%size_of_evaluators;

        int count_assignment=0;
//
        for(int i=0; i<size_of_evaluators; i++){
            int assignemntPerCurrEvaluator=assignments_per_evaluator;
            if(i<extra_assignements){
                assignemntPerCurrEvaluator++;
            }
            for(int j=0; j<assignemntPerCurrEvaluator; j++){
                String evaluatoremail=evaluators.get(i).getEmail(); //email1
                String learnerName=assignedToNames[j];
                String emailSubject="New Assignments assigned for evaluation";
                String emailBody="Dear " + evaluators.get(i).getEvaluatorName() + ",\n\n"
                        + "You have been assigned a new assignment:\n"
                        + "Assignment Name: " + assignment.getAssignmentName() + "\n"
                        + "Student email is : " + learnerName + ". \n"
                        + "Please check your assignment details.\n\n" +
                        "The assignmentDueDate is" + assignment.getAssignmentDuedate() +"\n"
                        + "Best regards,\n"
                        + "Incture technologies Private Limited";
                emailSenderService.sendSimpleEmail(evaluatoremail, emailSubject, emailBody);

                Talent talent = talentRepository.findByEmail(learnerName)
                        .orElseThrow(() -> new IllegalStateException("Could not find talent"));

                EmployeewiseAssignment employeeAssignment = new EmployeewiseAssignment();
                employeeAssignment.setEmployeeAssignmentId(generateRandomInt());
                employeeAssignment.setEmployeeEmail(learnerName);
                employeeAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
                employeeAssignment.setAssignmentName(assignment.getAssignmentName());
                employeeAssignment.setAssignmentTechnology(assignment.getAssignmentTechnology());
                employeeAssignment.setAssignmentDuedate(assignment.getAssignmentDuedate());
                employeeAssignment.setAssignmentFileName(assignment.getAssignmentFileName());
                employeeAssignment.setAssignmentStatus(assignment.getAssignmentStatus());
                employeeAssignment.setAssignmentFileUrl(assignment.getAssignmentFileUrl());
                employeeAssignment.setEmployeeAssignmentScore(0);
                employeeAssignment.setMaxmarks(assignment.getMaxmarks());
                employeeAssignment.setEmployeeAssignmentFeedback("Not checked yet");
                employeeAssignment.setEmployeeAssignmentFileUrl("Not submitted yet");
                employeeAssignment.setEvaluatorName(evaluators.get(i).getEvaluatorName());
                employeewiseAssignmentRepo.save(employeeAssignment);
                String emailBody1 = "Dear " + talent.getTalentName() + ",\n\n"
                        + "You have been assigned a new assignment. Please find the details below:\n\n"
                        + "Assignment Name: " + assignment.getAssignmentName() + "\n"
                        + "Due Date: " + assignment.getAssignmentDuedate() + "\n"
                        + "File Link: " + assignment.getAssignmentFileUrl() + "\n\n"
                        + "Best regards,\n"
                        + "Your Organization";

                // Send email to talent
                emailSenderService.sendSimpleEmail(talent.getEmail(), "New Assignment Assigned", emailBody1);

            }

        }
        //
        String trainerEmail=assignment.getTrainer();
        String emailSubject1="New Assignment Assigned for evaluation";
        String emailBody1="An assignment is assigned for evaluation for week" + "\n\n" + assignment.getAssignmentWeek();
        emailSenderService.sendSimpleEmail(trainerEmail, emailSubject1, emailBody1);


        return savedAssignment;
    }


    public List<Assignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    public Optional<Assignment> getAssignmentById(int id) {

        return assignmentRepo.findById(id);

    }

//    public Assignment updateAssignment(int id, Assignment assignment,  MultipartFile assignmentFile) throws IOException {
//        if (assignmentRepo.existsById(id)) {
//            Assignment existingAssignment = assignmentRepo.findById(id).orElse(null);
//            if (existingAssignment != null) {
//                existingAssignment.setAssignmentName(assignment.getAssignmentName());
//                existingAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
//                existingAssignment.setAssignmentDate(assignment.getAssignmentDate());
//                existingAssignment.setAssignmentScore(assignment.getAssignmentScore());
//                existingAssignment.setAssignmentStatus(assignment.getAssignmentStatus());
//                existingAssignment.setAssignmentFile(assignmentFile.getBytes());
//                return assignmentRepo.save(existingAssignment);
//            }
//        }
//        return null;
//
//    }

    public void deleteAssignmentById(int id) {
        assignmentRepo.deleteById(id);

    }
    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }
    public Assignment updateAssignment(int id, Assignment assignment) {
        Optional<Assignment> existingAssignmentOptional = assignmentRepo.findById(id);

        if (existingAssignmentOptional.isPresent()) {
            Assignment existingAssignment = existingAssignmentOptional.get();

            // Update assignment properties
            existingAssignment.setAssignmentWeek(assignment.getAssignmentWeek());
            existingAssignment.setAssignmentName(assignment.getAssignmentName());
            existingAssignment.setAssignmentTechnology(assignment.getAssignmentTechnology());
            existingAssignment.setAssignmentDuedate(assignment.getAssignmentDuedate());
            existingAssignment.setAssignedTo(assignment.getAssignedTo());

            // Save and return updated assignment
            return assignmentRepo.save(existingAssignment);
        } else {
            // Handle case when assignment with given id is not found
            throw new RuntimeException("Assignment not found with id: " + id);
        }
    }
}
