package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Mentor;
import com.incture.cpm.Service.MentorService;
import com.incture.cpm.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @GetMapping("/getMentorById/{mentorId}")
    public ResponseEntity<?> getMentorById(@PathVariable Long mentorId) {
        Optional<Mentor> mentor = mentorService.getMentorById(mentorId);
        if (mentor.isPresent()) {
            return ResponseEntity.ok(mentor.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Mentor found with given ID");
        }
    }

    @GetMapping("/getAllMentors")
    public ResponseEntity<List<Mentor>> getAllMentors() {
        List<Mentor> mentors = mentorService.getAllMentors();
        return ResponseEntity.ok(mentors);
    }

    @GetMapping("/getMentorByTeam/{teamId}")
    public ResponseEntity<?> getMentorsOfTeam(@PathVariable Long teamId) {
        try {
            List<Mentor> mentors = mentorService.getMentorsOfTeam(teamId);
            return ResponseEntity.ok(mentors);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/addMentor")
    public ResponseEntity<?> addMentor(@RequestParam Long talentId, @RequestParam Long teamId) {
        try {
            Mentor result = mentorService.addMentor(talentId, teamId);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding mentor: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{mentorId}")
    public ResponseEntity<?> deleteMentor(@PathVariable Long mentorId) {
        try {
            String result = mentorService.deleteMentor(mentorId);
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting mentor: " + ex.getMessage());
        }
    }

    @PutMapping("/update/{mentorId}")
    public ResponseEntity<?> updateMentor(@PathVariable Long mentorId, @RequestBody Mentor updateMentor) {
        try {
            String result = mentorService.updateMentor(mentorId, updateMentor);
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating mentor: " + ex.getMessage());
        }
    }
}
