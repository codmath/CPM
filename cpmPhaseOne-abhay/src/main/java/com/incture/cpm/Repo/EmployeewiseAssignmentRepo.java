package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.incture.cpm.Entity.EmployeewiseAssignment;

// Define the interface and extend JpaRepository with the appropriate generic types
public interface EmployeewiseAssignmentRepo extends JpaRepository<EmployeewiseAssignment, Integer> {

    Optional<List<EmployeewiseAssignment>> findByEmployeeEmail(String email);
    // You can define custom query methods here if needed
}
