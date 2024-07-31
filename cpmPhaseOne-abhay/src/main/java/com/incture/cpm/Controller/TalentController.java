package com.incture.cpm.Controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Dto.TalentSummaryDto;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.TalentService;
import com.incture.cpm.Service.PerformanceService;

@CrossOrigin("*")
@RestController
@RequestMapping("/cpm/talents")
public class TalentController {

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private TalentService talentService;

    @PostMapping("/createtalent")
    public ResponseEntity<Talent> createTalent(@RequestBody Talent talent) {
        Talent createdTalent = talentService.createTalent(talent);
        performanceService.addPerformanceWithTalent(createdTalent);
        return new ResponseEntity<>(createdTalent, HttpStatus.CREATED);
    }

    @PutMapping("/uploadmarksheet/{talentId}")
    public ResponseEntity<Talent> uploadMarksheets(@RequestPart MultipartFile marksheetsSemwise,
            @PathVariable Long talentId) throws SerialException, SQLException, IOException {
        Blob marksheetPdf = new SerialBlob(marksheetsSemwise.getBytes());
        Talent talent = talentService.getTalentById(talentId);
        talent.setMarksheetsSemwise(marksheetPdf);
        Talent updatedTalent = talentService.updateTalent(talent, talentId);
        if (updatedTalent != null) {
            performanceService.editTalentDetails(talent);
            return new ResponseEntity<>(updatedTalent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/uploadresume/{talentId}")
    public ResponseEntity<Talent> uploadResume(@RequestPart MultipartFile resume,
            @PathVariable Long talentId) throws SerialException, SQLException, IOException {
        Blob resumePdf = new SerialBlob(resume.getBytes());
        Talent talent = talentService.getTalentById(talentId);
        talent.setResume(resumePdf);
        Talent updatedTalent = talentService.updateTalent(talent, talentId);
        if (updatedTalent != null) {
            performanceService.editTalentDetails(talent);
            return new ResponseEntity<>(updatedTalent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addtalentfromcandidate")
    public ResponseEntity<Talent> addTalentFromCandidate(@RequestBody Candidate candidate) {
        Talent newtalent = talentService.addTalentFromCandidate(candidate);
        if (newtalent == null) {
            return new ResponseEntity<>(newtalent, HttpStatus.BAD_REQUEST);
        }
        if (performanceService.addPerformanceWithTalent(newtalent) == "Performance saved successfully")
            return new ResponseEntity<>(newtalent, HttpStatus.CREATED);
        return new ResponseEntity<>(newtalent, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/alltalent")
    public ResponseEntity<List<Talent>> getAllTalents() {
        List<Talent> talents = talentService.getAllTalents();
        return new ResponseEntity<>(talents, HttpStatus.OK);
    }

    @GetMapping("/gettalentbyid/{talentId}")
    public ResponseEntity<Talent> getTalentById(@PathVariable Long talentId) {
        Talent talent = talentService.getTalentById(talentId);
        if (talent != null) {
            return new ResponseEntity<>(talent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/updatetalent/{talentId}")
    public ResponseEntity<Talent> updateTalent(@RequestBody Talent talent, @PathVariable Long talentId) {
        Talent updatedTalent = talentService.updateTalent(talent, talentId);

        if (updatedTalent != null) {
            performanceService.editTalentDetails(updatedTalent);
            return new ResponseEntity<>(updatedTalent, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deletetalent/{talentId}")
    public ResponseEntity<Void> deleteTalent(@PathVariable Long talentId) {
        boolean check = talentService.deleteTalent(talentId);
        if (check) {
            performanceService.deletePerformance(talentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/viewmarksheet/{talentId}")
    public ResponseEntity<byte[]> getMarksheet(@PathVariable Long talentId) throws IOException {
        // Retrieve Talent object from the service layer
        Talent talent = talentService.getTalentById(talentId);

        if (talent != null && talent.getMarksheetsSemwise() != null) {
            try {
                // Retrieve the PDF data from the Talent object
                Blob marksheetBlob = talent.getMarksheetsSemwise();
                byte[] pdfData = marksheetBlob.getBytes(1, (int) marksheetBlob.length());

                // Set appropriate response headers for PDF content
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("marksheet.pdf").build());

                return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            } catch (SQLException e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/viewresume/{talentId}")
    public ResponseEntity<byte[]> getResume(@PathVariable Long talentId) throws IOException {
        // Retrieve Talent object from the service layer
        Talent talent = talentService.getTalentById(talentId);

        if (talent != null && talent.getResume() != null) {
            try {
                // Retrieve the PDF data from the Talent object
                Blob marksheetBlob = talent.getResume();
                byte[] pdfData = marksheetBlob.getBytes(1, (int) marksheetBlob.length());

                // Set appropriate response headers for PDF content
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("resume.pdf").build());

                return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            } catch (SQLException e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // newly added resignation functionality

    @PutMapping("/resign/{talentId}")
    public ResponseEntity<?> resignTalent(@PathVariable("talentId") Long talentId,
            @RequestParam String talentStatus,
            @RequestParam String exitReason,
            @RequestParam String exitDate,
            @RequestParam("otherReason") String exitComment) {
        try {
            Talent updatedTalent = talentService.resignTalent(talentId,
                    talentStatus,
                    exitReason, exitDate, exitComment);
            return ResponseEntity.ok(updatedTalent);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO talent found with the given id");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error Occurred while updating talent status");
        }
    }

    // Summary stats for talent table
    @GetMapping("/summary")
    public TalentSummaryDto getEmployeeSummary() {
        return talentService.talentStats();
    }
}