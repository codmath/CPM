package com.incture.cpm.Service;

import com.incture.cpm.Entity.TalentAssessment;
import com.incture.cpm.Dto.TalentAssessmentDto;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.TalentAssessmentRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.helper.AssessmentHelper;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;

@Service
public class TalentAssessmentService {

    @Autowired
    private TalentAssessmentRepository assessmentRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TalentService talentService;

    @Autowired
    private PerformanceService performanceService;

    // isse page par sab talents ki info display hogi
    public List<Talent> getAllTalentsForAssessment() {
        return talentService.getAllTalents();
    }

    // // Assessmnet Add krne ke liye
    public String addAssessment(TalentAssessmentDto assessment) {
        Optional<Talent> talent = talentRepository.findById(assessment.getTalentId());

        if (talent.isPresent()) {
            String msg = saveIndividually(assessment);
            performanceService.updateAssessmentScore(assessment.getTalentId());
            return msg;
        }
        return "Talent Id not found";
    }

    // view all assessments for a particular talent
    public List<TalentAssessment> viewAssessmentsForTalent(Long talentId) {
        Optional<List<TalentAssessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            return assessments.get();
        }
        return null;
    }

    public TalentAssessment updateTalentAssessment(Long assessmentId, Long talentId, double score, String reason) {
        Optional<TalentAssessment> assessment = assessmentRepository.findByAssessmentIdAndTalentId(assessmentId,
                talentId);

        if (assessment.isPresent()) {
            TalentAssessment talentAssessment = assessment.get();
            List<Double> marks = talentAssessment.getScores();
            int size = marks.size();
            marks.set(size - 1, score);
            talentAssessment.setScores(marks);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            talentAssessment.setComments(
                    formattedDateTime + ": Assessment Score Updated Manually= " + score + "(" + reason + ")" + "\n"
                            + talentAssessment.getComments());
            return assessmentRepository.save(talentAssessment);
        }
        return null;
    }

    // delete details of a particular assessment for a particular talent
    public boolean deleteAssessment(Long assessmentId, Long talentId) {
        Optional<List<TalentAssessment>> assessments = assessmentRepository.findAllByTalentId(talentId);

        if (assessments.isPresent()) {
            List<TalentAssessment> assessmentList = assessments.get();
            for (TalentAssessment assessment : assessmentList) {
                if (assessment.getAssessmentId() == assessmentId && assessment.getTalentId() == talentId) {
                    assessmentRepository.deleteById(assessment.getLocalKey());
                    performanceService.updateAssessmentScore(talentId);
                    return true;
                }
            }
        }
        return false;
    }

    public String save(MultipartFile file) {
        try {
            List<TalentAssessmentDto> assessmentList = AssessmentHelper
                    .convertExcelToAssessmentRecord(file.getInputStream());

            for (TalentAssessmentDto assessment : assessmentList) {
                @SuppressWarnings("unused")
                String msg = saveIndividually(assessment);
            }
            return "Assessment Data Saved Successfully";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some error occurred: ";
    }

    @Transactional
    public String saveIndividually(TalentAssessmentDto assessment) {
        try {
            Optional<TalentAssessment> assessmentRepoFetch = assessmentRepository
                    .findByAssessmentIdAndTalentId(assessment.getAssessmentId(), assessment.getTalentId());

            TalentAssessment talentAssessment = new TalentAssessment();
            talentAssessment.setTalentId(assessment.getTalentId());
            talentAssessment.setAssessmentId(assessment.getAssessmentId());
            talentAssessment.setAssessmentType(assessment.getAssessmentType());
            talentAssessment.setAssessmentSkill(assessment.getAssessmentSkill());
            talentAssessment.setLocation(assessment.getLocation());
            talentAssessment.setAssessmentDate(assessment.getAssessmentDate());
            if (assessmentRepoFetch.isPresent()) {
                TalentAssessment existingAssessment = assessmentRepoFetch.get();

                talentAssessment.setLocalKey(existingAssessment.getLocalKey());
                talentAssessment.setAttempts(existingAssessment.getAttempts() + 1);
                List<Double> marks = existingAssessment.getScores();
                marks.add(assessment.getScore());
                talentAssessment.setScores(marks);

                // Setting the Logs of Assessment Updates
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                talentAssessment.setComments(formattedDateTime + ": Assessment Score Updated= "
                        + assessment.getScore() + "\n" + existingAssessment.getComments());

                assessmentRepository.save(talentAssessment);
                performanceService.updateAssessmentScore(talentAssessment.getTalentId());
            } else {

                talentAssessment.setAttempts(1);
                List<Double> mark = new ArrayList<>();
                mark.add(assessment.getScore());
                talentAssessment.setScores(mark);

                // Setting the Logs of Assessment Creation
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = now.format(formatter);
                talentAssessment.setComments(
                        formattedDateTime + ": Assessment Score Declared= " + assessment.getScore() + "\n");

                assessmentRepository.save(talentAssessment);
                performanceService.updateAssessmentScore(assessment.getTalentId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Some error occurred: " + e.getMessage();
        }
        return "Assessment record saved!";
    }

    public List<TalentAssessment> getUniqueAssessments() {
        List<TalentAssessment> allAssessments = assessmentRepository.findAll();
        Map<Long, TalentAssessment> uniqueAssessmentsMap = new HashMap<>();
        for (TalentAssessment assessment : allAssessments) {
            uniqueAssessmentsMap.putIfAbsent(assessment.getAssessmentId(), assessment);
        }
        return List.copyOf(uniqueAssessmentsMap.values());
    }

    public List<TalentAssessment> getAllAssessmentsByAssessmentId(Long assessmentId) {
        return assessmentRepository.findAllByAssessmentId(assessmentId);
    }

}
