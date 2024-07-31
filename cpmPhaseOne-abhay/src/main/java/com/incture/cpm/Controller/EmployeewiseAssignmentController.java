package com.incture.cpm.Controller;

import com.incture.cpm.Entity.EmployeewiseAssignment;
import com.incture.cpm.Service.EmployeewiseAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee-assignments")
@CrossOrigin("*")
public class EmployeewiseAssignmentController {

    @Autowired
    private EmployeewiseAssignmentService employeewiseAssignmentService;

    @GetMapping("/getall")
    public ResponseEntity<List<EmployeewiseAssignment>> getAllEmployeeAssignments() {
        List<EmployeewiseAssignment> employeeAssignments = employeewiseAssignmentService.getAllEmployeeAssignments();
        return new ResponseEntity<>(employeeAssignments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeewiseAssignment> getEmployeeAssignmentById(@PathVariable int id) {
        EmployeewiseAssignment employeeAssignment = employeewiseAssignmentService.getEmployeeAssignmentById(id);
        if (employeeAssignment != null) {
            return new ResponseEntity<>(employeeAssignment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update/{id}/{sendFeedback}")
    public ResponseEntity<EmployeewiseAssignment> updateEmployeeAssignment(@PathVariable int id, @PathVariable boolean sendFeedback, @RequestBody EmployeewiseAssignment updatedEmployeeAssignment) {
        EmployeewiseAssignment employeeAssignment = employeewiseAssignmentService.updateEmployeeAssignment(id, updatedEmployeeAssignment, sendFeedback);
        if (employeeAssignment != null) {
            return new ResponseEntity<>(employeeAssignment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployeeAssignmentById(@PathVariable int id) {
        employeewiseAssignmentService.deleteEmployeeAssignmentById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
