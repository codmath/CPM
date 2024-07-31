package com.incture.cpm.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Attendance;
import com.incture.cpm.Exception.BadRequestException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.AttendanceService;

@RestController
@RequestMapping("/cpm/attendance")
@CrossOrigin("*")
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;

    // used to get all attendance list
    @GetMapping("/getAllAttendance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }

    // used in daily view
    @GetMapping("/getAttendanceByDate")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDate(@RequestParam String date) {
        if (date == null || date.isEmpty()) {
            throw new BadRequestException("Date parameter is required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAttendanceByDate(date);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the date: " + date);
        }
    }

    // used temporarily for adding attendance
    @PostMapping("/addAttendance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addAttendance(@RequestBody Attendance attendance) {
        if (attendance == null) {
            throw new BadRequestException("Attendance data is required.");
        }
        try {
            String message = attendanceService.addAttendance(attendance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // used temporarily for adding attendance list
    @PostMapping("/addAttendanceByList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addAttendanceByList(@RequestBody List<Attendance> attendance) {
        if (attendance == null || attendance.isEmpty()) {
            throw new BadRequestException("Attendance list is required.");
        }
        try {
            String message = attendanceService.addAttendanceByList(attendance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // used in weekly and monthly view
    @GetMapping("/getAttendanceByDateRangeAndTalent")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDateRangeAndTalent(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam Long talentId) {
        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty() || talentId == null) {
            throw new BadRequestException("Start date, end date, and talent ID are required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAttendanceByDateRangeAndTalent(startDate, endDate, talentId);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the given date range and talent ID: " + talentId);
        }
    }

    // give filtered attendance data w.r.t to the reporting manager
    @GetMapping("/getAllAttendanceWRTrm")
    public ResponseEntity<Optional<List<Attendance>>> getAttendanceByDateRM(
            @RequestParam String date,
            @RequestParam("rm") String reportingManager) {
        if (date == null || date.isEmpty() || reportingManager == null || reportingManager.isEmpty()) {
            throw new BadRequestException("Date and reporting manager are required.");
        }
        Optional<List<Attendance>> attendanceList = attendanceService.getAllAttendanceRM(date, reportingManager);

        if (attendanceList.isPresent()) {
            return ResponseEntity.ok(attendanceList);
        } else {
            throw new ResourceNotFoundException("No attendance found for the date: " + date + " and reporting manager: " + reportingManager);
        }
    }
}
