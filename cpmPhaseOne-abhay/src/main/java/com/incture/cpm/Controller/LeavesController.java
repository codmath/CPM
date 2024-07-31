package com.incture.cpm.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.incture.cpm.Entity.Leaves;
import com.incture.cpm.Service.LeavesService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/leaves")
public class LeavesController {
    @Autowired
    private LeavesService leavesService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Leaves> getLeaves() {
        return leavesService.getAll();
    }

    @PostMapping("/addLeave")
    public ResponseEntity<String> addLeave(@RequestPart("leave") Leaves leave,
                                    @RequestPart("file") MultipartFile file) throws SerialException, SQLException, IOException{
        String message = leavesService.addLeave(leave, file);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/addLeaves")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addLeaves(@RequestBody List<Leaves> leaves){
        String message = leavesService.addLeaves(leaves);
        return new ResponseEntity<>(message, HttpStatus.OK);
  }

    @PutMapping("/approve/{leaveId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> approve(@PathVariable Long leaveId) {
        String message = leavesService.approve(leaveId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/decline/{leaveId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> decline(@PathVariable Long leaveId, @RequestParam String reasonForReject) {
        String message = leavesService.decline(leaveId, reasonForReject);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
