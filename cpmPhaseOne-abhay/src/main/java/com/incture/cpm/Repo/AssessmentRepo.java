package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Assessment;
import com.incture.cpm.Entity.CollegeTPO;

@Repository
public interface AssessmentRepo extends JpaRepository<Assessment, Long>{
    Optional<Assessment> findByEmail(String email);
    Optional<List<Assessment>> findAllByCollege(CollegeTPO college);
}
