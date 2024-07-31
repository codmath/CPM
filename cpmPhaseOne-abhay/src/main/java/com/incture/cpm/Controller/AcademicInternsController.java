package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.AcademicInterns;
import com.incture.cpm.Service.AcademicInternsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cpm/api/attendance")
@CrossOrigin("*")
public class AcademicInternsController {
    @Autowired
    private AcademicInternsService csvService;
 
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSVFile(@RequestParam MultipartFile file) {
        // Check if the file is empty
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
 
        // Check if the file is a CSV
        if (!file.getContentType().equals("text/csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is not a CSV");
        }
 
        try {
            csvService.readCSV(file);
            return ResponseEntity.status(HttpStatus.OK).body("CSV file processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing the CSV file: " + e.getMessage());
        }
    }
 
    @GetMapping("/getAll")
    public ResponseEntity<List<AcademicInterns>> getAttendance() {
        return ResponseEntity.ok(csvService.getAllAttendanceRecords());
    }
 
    @GetMapping("/getAttendanceByDate")
    public ResponseEntity<Optional<List<AcademicInterns>>> getAttendanceByDate(@RequestParam String date) {
        Optional<List<AcademicInterns>> attendanceList = csvService.getAttendanceByDate(date);
 
        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
 
    @PutMapping("/updateAttendanceById/{id}")
    public ResponseEntity<AcademicInterns> updateAttendance(@PathVariable Long id, @RequestBody AcademicInterns updatedAttendance) {
        Optional<AcademicInterns> existingAttendanceOptional = csvService.getAttendanceById(id);
        if (existingAttendanceOptional.isPresent()) {
            AcademicInterns updatedRecord = csvService.updateAttendance(id, updatedAttendance);
            return ResponseEntity.ok(updatedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
 
    @PostMapping("/addInternAttendance")
    public ResponseEntity<AcademicInterns> addAttendanceManually(@RequestBody AcademicInterns attendance) {
        return ResponseEntity.ok(csvService.addAttendanceManually(attendance));
    }
}