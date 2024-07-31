package com.incture.cpm.Repo;


import com.incture.cpm.Entity.Interviewer;
import com.incture.cpm.Entity.InterviewerScheduling;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntervieweScheduleRepo extends JpaRepository<InterviewerScheduling, Integer> {
    List<InterviewerScheduling> findByInterviewer(Interviewer interviewer);
    List<InterviewerScheduling> findByInterviewer_InterviewerId(String interviewerId);
    @EntityGraph(attributePaths = {"collegeTPO", "interviewer"})
    List<InterviewerScheduling> findAll();


}
