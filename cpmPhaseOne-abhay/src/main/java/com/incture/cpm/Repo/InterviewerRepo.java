package com.incture.cpm.Repo;

import com.incture.cpm.Entity.Interviewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewerRepo extends JpaRepository<Interviewer, String> {
    Optional<Interviewer> findByInterviewerId(String interviewerId);
}
