package com.incture.cpm.Service;

//import com.example.CampusCalendar.Entities.CollegeAllocation;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Entity.Interviewer;
import com.incture.cpm.Entity.InterviewerScheduling;
import com.incture.cpm.Exception.InterviewerSchedulingNotFoundException;
import com.incture.cpm.Repo.CollegeTPORepo;
import com.incture.cpm.Repo.IntervieweScheduleRepo;
import com.incture.cpm.Repo.InterviewerRepo;
import com.incture.cpm.config.NotificationService;
import com.incture.cpm.Dto.InterviewerSchedulingDto;
import com.incture.cpm.Dto.InterviewerSchedulingUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.server.header.StaticServerHttpHeadersWriter;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
//import java.util.logging.Logger;

@Service
public class InterviewScheduleService {
    @Autowired
    private IntervieweScheduleRepo intervieweScheduleRepo;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private InterviewerRepo interviewerRepo;
    @Autowired
    private CollegeTPORepo collegeTPORepo;
    private static final Logger logger = LoggerFactory.getLogger(InterviewScheduleService.class);

    public InterviewerScheduling creatingFunction(InterviewerScheduling interviewerScheduling) {
        CollegeTPO collegeTPO = interviewerScheduling.getCollegeTPO();
        interviewerScheduling.setCollegeTPO(collegeTPO);
        String interviewerId = interviewerScheduling.getInterviewer().getInterviewerId();
        Interviewer interviewer = interviewerRepo.findById(interviewerId)
                .orElseThrow(() -> new RuntimeException("Interviewer not found"));

        InterviewerScheduling savedInterviewerScheduling = intervieweScheduleRepo.save(interviewerScheduling);
        if (!interviewer.getScheduleDetails().contains(savedInterviewerScheduling)) {
            interviewer.getScheduleDetails().add(savedInterviewerScheduling);
            interviewerRepo.save(interviewer);
        }
        // email notification
        String subject = "Interview Allocated for Campus Hiring";
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(interviewerScheduling.getInterviewer().getInterviewerName()).append(",\n\n")
                .append("You have to take the interview on\n\n").append(interviewerScheduling.getInterviewDate())
                .append(", \n\n")
                .append("Interview College:\n").append(interviewerScheduling.getCollegeTPO().getCollegeName());

        notificationService.sendSimpleNotification(interviewerScheduling.getInterviewer().getEmail(), subject,
                body.toString());

        return savedInterviewerScheduling;
    }

    public List<InterviewerScheduling> finding() {
        return this.intervieweScheduleRepo.findAll();
    }

    @Transactional
    public void deleteSchedule(int scheduleId) {
        InterviewerScheduling existingInterviewerScheduling = intervieweScheduleRepo.findById(scheduleId)
                .orElseThrow(
                        () -> new EntityNotFoundException("InterviewerScheduling not found with id: " + scheduleId));

        Interviewer interviewer = existingInterviewerScheduling.getInterviewer();
        if (interviewer != null) {
            interviewer.getScheduleDetails().remove(existingInterviewerScheduling); // Remove the association from the
                                                                                    // parent side
            interviewerRepo.save(interviewer);
        }
        existingInterviewerScheduling.setCollegeTPO(null);
        existingInterviewerScheduling.setInterviewer(null);
        intervieweScheduleRepo.save(existingInterviewerScheduling);
        intervieweScheduleRepo.delete(existingInterviewerScheduling); // Delete the child entity
    }

    public List<InterviewerSchedulingDto> getAllInterviewerSchedulings() {
        List<InterviewerScheduling> schedulings = intervieweScheduleRepo.findAll();
        return schedulings.stream().map(scheduling -> {
            InterviewerSchedulingDto dto = new InterviewerSchedulingDto();
            if (scheduling.getCollegeTPO() != null) {
                dto.setCollegeName(scheduling.getCollegeTPO().getCollegeName());
                dto.setLocation(scheduling.getCollegeTPO().getLocation());
                dto.setRegion(scheduling.getCollegeTPO().getRegion());
            } else {
                dto.setCollegeName("Not assigned");
                dto.setLocation("Not assigned");
                dto.setRegion("Not assigned");
            }
            dto.setInterviewerName(
                    (scheduling.getInterviewer() != null) ? scheduling.getInterviewer().getInterviewerName()
                            : "Not assigned");
            dto.setGrade(
                    ((scheduling.getInterviewer() != null) ? scheduling.getInterviewer().getGrade() : "Not assigned"));
            dto.setInterviewDate(scheduling.getInterviewDate());
            dto.setScheduleId(scheduling.getScheduleId());
            dto.setPptDate(scheduling.getPptDate());
            dto.setAssessmentDate(scheduling.getAssessmentDate());
            dto.setDesignDate(scheduling.getDesignDate());
            return dto;
        }).collect(Collectors.toList());
    }

    public InterviewerScheduling updateInterviewerScheduling(InterviewerSchedulingUpdateDto dto) {
        Optional<InterviewerScheduling> schedulingOpt = intervieweScheduleRepo.findById(dto.getScheduleId());

        if (schedulingOpt.isPresent()) {
            InterviewerScheduling scheduling = schedulingOpt.get();

            // Update the fields
            if (dto.getCollegeId() != 0) {
                Optional<CollegeTPO> collegeTPOOpt = collegeTPORepo.findById(dto.getCollegeId());
                collegeTPOOpt.ifPresent(scheduling::setCollegeTPO);
            }

            if (dto.getInterviewerId() != null) {
                Optional<Interviewer> interviewerOpt = interviewerRepo.findById(dto.getInterviewerId());
                interviewerOpt.ifPresent(scheduling::setInterviewer);
            }
            scheduling.setPptDate(dto.getPptDate());
            scheduling.setAssessmentDate(dto.getAssessmentDate());
            scheduling.setDesignDate(dto.getDesignDate());
            scheduling.setInterviewDate(dto.getInterviewDate());
            return intervieweScheduleRepo.save(scheduling);
        } else {
            throw new InterviewerSchedulingNotFoundException("not found");
        }
    }

    public InterviewerScheduling update(InterviewerScheduling interviewerScheduling, int myId) {
        Optional<InterviewerScheduling> optionalExistingInterviewerScheduling = intervieweScheduleRepo.findById(myId);
        if (optionalExistingInterviewerScheduling.isPresent()) {
            InterviewerScheduling existingInterviewerScheduling = optionalExistingInterviewerScheduling.get();

            // Update the existing entity with new values
            if (interviewerScheduling.getCollegeTPO() != null) {
                CollegeTPO updatedCollegeTPO = collegeTPORepo
                        .findById(interviewerScheduling.getCollegeTPO().getCollegeId())
                        .orElse(interviewerScheduling.getCollegeTPO());

                existingInterviewerScheduling.setCollegeTPO(updatedCollegeTPO);
            }
            if (interviewerScheduling.getPptDate() != null) {
                existingInterviewerScheduling.setPptDate(interviewerScheduling.getPptDate());
            }
            if (interviewerScheduling.getAssessmentDate() != null) {
                existingInterviewerScheduling.setAssessmentDate(interviewerScheduling.getAssessmentDate());
            }
            if (interviewerScheduling.getDesignDate() != null) {
                existingInterviewerScheduling.setDesignDate(interviewerScheduling.getDesignDate());
            }
            if (interviewerScheduling.getInterviewDate() != null) {
                existingInterviewerScheduling.setInterviewDate(interviewerScheduling.getInterviewDate());
            }
            if (interviewerScheduling.getInterviewer() != null) {
                Interviewer updatedInterviewer = interviewerRepo
                        .findById(interviewerScheduling.getInterviewer().getInterviewerId())
                        .orElse(interviewerScheduling.getInterviewer());
                existingInterviewerScheduling.setInterviewer(updatedInterviewer);
            }
            // Save the updated entity
            return intervieweScheduleRepo.save(existingInterviewerScheduling);
        } else {
            throw new EntityNotFoundException("InterviewerScheduling not found with id: " + myId);
        }
    }
}
