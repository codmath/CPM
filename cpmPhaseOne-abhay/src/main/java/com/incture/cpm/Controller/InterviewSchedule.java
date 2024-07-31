package com.incture.cpm.Controller;

import com.incture.cpm.Entity.InterviewerScheduling;
import com.incture.cpm.Service.InterviewScheduleService;
import com.incture.cpm.Dto.InterviewerSchedulingDto;
import com.incture.cpm.Dto.InterviewerSchedulingUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admin/interviewschedule")
@CrossOrigin(origins = "*")
public class InterviewSchedule {
    @Autowired
    private InterviewScheduleService interviewScheduleService;
    @PostMapping("/create")
    public ResponseEntity<InterviewerScheduling> insertingFunction(@RequestBody InterviewerScheduling interviewerScheduling){
        return ResponseEntity.ok(interviewScheduleService.creatingFunction(interviewerScheduling));

    }
    //
    @GetMapping("/readAll")
    public List<InterviewerScheduling> readingFunction(){

        return  interviewScheduleService.finding();
    }
//
    @DeleteMapping("/delete/{scheduleId}")
    public ResponseEntity<String> deletingFunction(@PathVariable int scheduleId){
        interviewScheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok("Data deleted Successfully");
    }
    //
    @GetMapping("/read")
    public ResponseEntity<List<InterviewerSchedulingDto>> getAllInterviewerSchedulings() {
        List<InterviewerSchedulingDto> schedulings = interviewScheduleService.getAllInterviewerSchedulings();
        return ResponseEntity.ok(schedulings);
    }
    //
    @PutMapping("/update/{scheduleId}")
    public ResponseEntity<InterviewerScheduling> updateInterviewerScheduling(@RequestBody InterviewerSchedulingUpdateDto dto, @PathVariable int scheduleId) {
        dto.setScheduleId(scheduleId);
        InterviewerScheduling updatedScheduling =interviewScheduleService.updateInterviewerScheduling(dto);
        return ResponseEntity.ok(updatedScheduling);
    }
//
@PutMapping("/updateScheduling/{schId}")
    public  ResponseEntity<InterviewerScheduling> update(@RequestBody InterviewerScheduling interviewerScheduling, @PathVariable int schId ){
       InterviewerScheduling updatedInterviewScheduling= interviewScheduleService.update(interviewerScheduling, schId);
        return ResponseEntity.ok(updatedInterviewScheduling);
}
}
