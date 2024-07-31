package com.incture.cpm.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.AssessmentLevelOne;
import com.incture.cpm.Entity.AssessmentLevelTwo;
import com.incture.cpm.Entity.AssessmentLevelThree;
import com.incture.cpm.Entity.AssessmentLevelFour;
import com.incture.cpm.Entity.AssessmentLevelFive;
import com.incture.cpm.Service.AssessmentService;
import com.incture.cpm.Util.ExcelUtil;

@RestController
@RequestMapping("/cpm2/assessment")
@CrossOrigin("*")
public class AsssessmentController {
    @Autowired
    AssessmentService assessmentService;

    @Autowired
    ExcelUtil excelUtil;
    @PostMapping("/loadCandidates")
    public ResponseEntity<?> loadCandidates(@RequestParam int collegeId){
        return new ResponseEntity<>(assessmentService.loadCandidates(collegeId), HttpStatus.OK);
    }
    @PostMapping("/uploadLevelOne")
    public ResponseEntity<?> uploadLevelOne(@RequestParam MultipartFile file, @RequestParam int collegeId){
        System.out.println("inside file");
        if (file.isEmpty())  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file");
        
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            System.out.println("go");
            this.assessmentService.newUploadLevelOne(file, collegeId);
            return ResponseEntity.ok(Map.of("message", "File is uploaded and data is saved to db"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a valid Excel file");
        } 
    }
 
    @GetMapping("/getAssessmentByCollegeId")
    public ResponseEntity<?> getAssessmentByCollegeId(@RequestParam int collegeId){
        return new ResponseEntity<>(assessmentService.getAssessmentByCollegeId(collegeId), HttpStatus.OK);
    }

    @GetMapping("/getAllAssessments")
    public ResponseEntity<?> getAllAssessments(){
        return new ResponseEntity<>(assessmentService.getAllAssessments(), HttpStatus.OK);
    }
    
    @PostMapping("/selectLevelOne")
    public ResponseEntity<String> selectLevelOne(@RequestBody List<AssessmentLevelOne> levelOneSelectedList){
        String message = assessmentService.selectLevelOne(levelOneSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/selectLevelTwo")
    public ResponseEntity<String> selectLevelTwo(@RequestBody List<AssessmentLevelTwo> levelTwoSelectedList){
        String message = assessmentService.selectLevelTwo(levelTwoSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/selectLevelThree")
    public ResponseEntity<String> selectLevelThree(@RequestBody List<AssessmentLevelThree> levelThreeSelectedList){
        String message = assessmentService.selectLevelThree(levelThreeSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/selectLevelFour")
    public ResponseEntity<String> selectLevelFour(@RequestBody List<AssessmentLevelFour> levelFourSelectedList){
        String message = assessmentService.selectLevelFour(levelFourSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/selectLevelFive")
    public ResponseEntity<String> selectLevelFive(@RequestBody List<AssessmentLevelFive> levelFiveSelectedList){
        String message = assessmentService.selectLevelFive(levelFiveSelectedList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/updateLevelOne")
    public ResponseEntity<String> updateLevelOne(@RequestBody AssessmentLevelOne levelOne){
        try {
            String message = assessmentService.updateLevelOne(levelOne);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateLevelTwo")
    public ResponseEntity<String> updateLevelTwo(@RequestBody AssessmentLevelTwo levelTwo){
        try {
            String message = assessmentService.updateLevelTwo(levelTwo);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateLevelThree")
    public ResponseEntity<String> updateLevelThree(@RequestBody AssessmentLevelThree levelThree){
        try {
            String message = assessmentService.updateLevelThree(levelThree);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateLevelFour")
    public ResponseEntity<String> updateLevelFour(@RequestBody AssessmentLevelFour levelFour){
        try {
            String message = assessmentService.updateLevelFour(levelFour);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateLevelFive")
    public ResponseEntity<String> updateLevelFive(@RequestBody AssessmentLevelFive levelFive){
        try {
            String message = assessmentService.updateLevelFive(levelFive);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IllegalArgumentException  e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/readExcel") // read and show
    public ResponseEntity<List<Map<String, String>>> readExcel(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(excelUtil.readExcelFile(file));
    }
}
