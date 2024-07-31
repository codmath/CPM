package com.incture.cpm.Service;

import com.incture.cpm.Entity.Interviewer;
import com.incture.cpm.Entity.InterviewerScheduling;
import com.incture.cpm.Exception.InterviewerNotFoundException;
import  com.incture.cpm.Repo.IntervieweScheduleRepo;
import  com.incture.cpm.Repo.InterviewerRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import  com.incture.cpm.Dto.InterviewerDto;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewerService {
    @Autowired
    private InterviewScheduleService interviewScheduleService;
    @Autowired
    private InterviewerRepo interviewerRepo;
    @Autowired
    private IntervieweScheduleRepo intervieweScheduleRepo;
    //
    public Interviewer insertInterviewData(Interviewer interviewer) {
        return this.interviewerRepo.save(interviewer);
    }
    //
        public InterviewerDto readingFunction(Interviewer interviewer) {
        List<InterviewerScheduling> interviewerScheduling = interviewer.getScheduleDetails();
        interviewerScheduling.forEach(interviewerScheduling1 -> interviewerScheduling1.setInterviewer(null));
            return new InterviewerDto(
                    interviewer.getInterviewerId(),
                    interviewer.getInctureId(),
                    interviewer.getInterviewerName(),
                    interviewer.getGrade(),
                    interviewer.getTechRole(),
                    interviewer.getTechProficiency(),
                    interviewer.getLocation(),
                    interviewer.getRegion(),
                    interviewer.getWorkExperience(),
                    interviewer.getEmail(),
                    interviewer.getMobileNumber(),
//                    interviewer.getScheduleDetails()
                    interviewer.getPrevBatches(),
                    interviewerScheduling
            );
        }

        public List<InterviewerDto> toDTOList(List<Interviewer> interviewers) {
            return interviewers.stream()
                    .map(this::readingFunction)
                    .collect(Collectors.toList());
        }

//
    public Interviewer updatingFunction(Interviewer interviewer, String interviewerId) {
        Optional<Interviewer> existingInterviewerOptionsl=interviewerRepo.findById(interviewerId);
        if(existingInterviewerOptionsl.isPresent()){
            Interviewer existingInterviewer=existingInterviewerOptionsl.get();
            existingInterviewer.setInctureId(interviewer.getInctureId());
            existingInterviewer.setInterviewerName(interviewer.getInterviewerName());
            existingInterviewer.setRegion(interviewer.getRegion());
            existingInterviewer.setLocation(interviewer.getLocation());
            existingInterviewer.setGrade(interviewer.getGrade());
            existingInterviewer.setTechRole(interviewer.getTechRole());
            existingInterviewer.setTechProficiency(interviewer.getTechProficiency());
            existingInterviewer.setWorkExperience(interviewer.getWorkExperience());
            existingInterviewer.setPrevBatches(interviewer.getPrevBatches());
            existingInterviewer.setEmail(interviewer.getEmail());
            existingInterviewer.setMobileNumber(interviewer.getMobileNumber());
            return interviewerRepo.save(existingInterviewer);
        }
        else {
            System.out.println("NOO valid id");
            throw new InterviewerNotFoundException("Not found");
        }
    }
//
    public List<InterviewerScheduling> readingFunctionById(String interviewerId) {
        Interviewer interviewer = interviewerRepo.findByInterviewerId(interviewerId).orElseThrow(()-> new RuntimeException("Entity not found"));
        return intervieweScheduleRepo.findByInterviewer(interviewer);
    }
@Transactional
public void deletes(String interviewerId) {
    // Find InterviewerScheduling entities referencing the Interviewer
     Interviewer interviewer = interviewerRepo.findById(interviewerId).orElseThrow();

    List<InterviewerScheduling> scheduleDetails = interviewer.getScheduleDetails();
    for (InterviewerScheduling schedule : scheduleDetails) {

   schedule.setInterviewer(null);
   intervieweScheduleRepo.save(schedule);
    }

    interviewer.setScheduleDetails(null); // Clear the association from the parent side
    interviewerRepo.save(interviewer);
    interviewerRepo.delete(interviewer); // Delete the parent entity
}

}

