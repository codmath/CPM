package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Assignment;
import com.incture.cpm.Entity.EmailRequest;
import com.incture.cpm.Service.AssignmentService;
import com.incture.cpm.Service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private EmailSenderService emailSenderService; // Autowire the EmailSenderService

    @PostMapping("/Create")
    public Assignment createAssignment(@RequestBody Assignment assignment) throws IOException {
        return assignmentService.createAssignment(assignment);
    }

    @GetMapping("/ReadAll")
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @GetMapping("/Read/{assign_id}")
    public Optional<Assignment> getAssignmentById(@PathVariable int assign_id) {
        return assignmentService.getAssignmentById(assign_id);
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        emailSenderService.sendSimpleEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getBody());
    }
    @DeleteMapping("/Delete/{assign_id}")
    public void deleteAssignmentById(@PathVariable int assign_id) {
        assignmentService.deleteAssignmentById(assign_id);
    }
    @PutMapping("/update/{id}")
    public Assignment updateAssignment(@PathVariable int id, @RequestBody Assignment assignment) {
        return assignmentService.updateAssignment(id, assignment);
    }
}
