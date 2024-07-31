package com.incture.cpm.Controller;

import com.incture.cpm.Entity.TalentAssessment;
import com.incture.cpm.Dto.TalentAssessmentDto;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Service.TalentAssessmentService;
import com.incture.cpm.helper.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/assessments")
public class TalentAssessmentController {

    @Autowired
    private TalentAssessmentService assessmentService;

    @GetMapping("/assementwiseview")
    public ResponseEntity<List<TalentAssessment>> getUniqueAssessments() {
        List<TalentAssessment> uniqueAssessments = assessmentService.getUniqueAssessments();
        return ResponseEntity.ok(uniqueAssessments);
    }

    @GetMapping("/getalltalents")
    public ResponseEntity<List<Talent>> getAllTalentsForAssessment() {
        List<Talent> talents = assessmentService.getAllTalentsForAssessment();
        return ResponseEntity.ok(talents);
    }

    @PostMapping("/addassessment")
    public ResponseEntity<String> addAssessment(@RequestBody TalentAssessmentDto assessment) {
        String response = assessmentService.addAssessment(assessment);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/viewassessment/{talentId}")
    public ResponseEntity<List<TalentAssessment>> viewAssessmentsForTalent(@PathVariable Long talentId) {
        List<TalentAssessment> assessments = assessmentService.viewAssessmentsForTalent(talentId);
        if (assessments != null) {
            return ResponseEntity.ok(assessments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateassessment/{assessmentId}/{talentId}")
    public ResponseEntity<TalentAssessment> updateAssessment(@RequestParam Long assessmentId,
            @RequestParam Long talentId, @RequestParam double score, @RequestParam String reason) {
        TalentAssessment assessment = assessmentService.updateTalentAssessment(assessmentId, talentId, score, reason);
        if (assessment != null) {
            return ResponseEntity.ok(assessment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteassessment/{assessmentId}/{talentId}")
    public ResponseEntity<String> deleteAssessment(@PathVariable Long assessmentId, @PathVariable Long talentId) {
        boolean deleted = assessmentService.deleteAssessment(assessmentId, talentId);
        if (deleted) {
            return ResponseEntity.ok("Assessment deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Assessment not found");
        }
    }

    // ********************Uploading Excel file for Assessment
    // Record*************************** */

    @PostMapping("/uploadexcel")
    public ResponseEntity<?> uploadExcelFile(@RequestPart MultipartFile file) {
        if (Helper.checkExcelFormat(file)) {
            String message = this.assessmentService.save(file);

            return ResponseEntity.ok(Map.of("message", message));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file ");
    }

    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<List<TalentAssessment>> getAllAssessmentsByAssessmentId(@PathVariable Long assessmentId) {
        List<TalentAssessment> assessments = assessmentService.getAllAssessmentsByAssessmentId(assessmentId);
        if (!assessments.isEmpty()) {
            return ResponseEntity.ok(assessments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}