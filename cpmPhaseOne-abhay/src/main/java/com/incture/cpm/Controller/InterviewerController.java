package com.incture.cpm.Controller;

import com.incture.cpm.Dto.InterviewerDto;
import com.incture.cpm.Entity.Interviewer;
import com.incture.cpm.Entity.InterviewerScheduling;
import com.incture.cpm.Repo.InterviewerRepo;
import com.incture.cpm.Service.InterviewerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/interviewer")
@CrossOrigin(origins = "*")
public class InterviewerController {
    @Autowired
    private InterviewerService interviewerService;
    @Autowired
    private InterviewerRepo interviewerRepo;
    @PostMapping("/create")
    public ResponseEntity<Interviewer> insertingFunction(@RequestBody Interviewer interviewer){
        return ResponseEntity.ok(interviewerService.insertInterviewData(interviewer));

    }

    //
    @GetMapping(value = "/read")
    public List<InterviewerDto> readingFunction(){
        List<Interviewer> interviewers=interviewerRepo.findAll();
        return interviewerService.toDTOList(interviewers);
    }
    @GetMapping("/read/{interviewerId}")
    public List<InterviewerScheduling> readingDunctionById(@PathVariable String interviewerId){
        return  interviewerService.readingFunctionById(interviewerId);
    }
    //
    @PutMapping("/update/{interviewerId}")
    public ResponseEntity<Interviewer> updatingFunction(@RequestBody Interviewer interviewer,@PathVariable String interviewerId){
        return ResponseEntity.ok(interviewerService.updatingFunction(interviewer, interviewerId));
    }
    //
    @DeleteMapping("/delete/{interviewerId}")
    public ResponseEntity<String> deletingFunction(@PathVariable String interviewerId){
        interviewerService.deletes(interviewerId);
        return  ResponseEntity.ok("Data Successfully Deleted");
    }
    }
