package com.incture.cpm.Service;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Leaves;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.LeavesRepo;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class LeavesService {
    @Autowired
    LeavesRepo leavesRepo;

    @Autowired
    TalentRepository talentRepo;

    @Autowired
    AttendanceService attendanceService;

    public List<Leaves> getAll(){
        return leavesRepo.findAll();
    }

    public String addLeave(Leaves leave) {
        Talent existingTalent = talentRepo.findById(leave.getTalentId()).orElseThrow(() -> new IllegalStateException("Talent does not exist for given talent id"));
        
        leave.setTalentName(existingTalent.getTalentName());
        leave.setApproverName(existingTalent.getReportingManager());
        leave.setApprovalStatus("Pending");

        leavesRepo.save(leave);
        return "Leave added Successfully";
    }
    
    public String addLeave(Leaves leave, MultipartFile file) throws SerialException, SQLException, IOException {
       Talent existingTalent = talentRepo.findById(leave.getTalentId())
                .orElseThrow(() -> new IllegalStateException("Talent does not exist for given talent id"));

        leave.setTalentName(existingTalent.getTalentName());
        leave.setApproverName(existingTalent.getReportingManager());
        leave.setApprovalStatus("Pending");

        try {
            if (file == null || file.isEmpty()) return "Failed to process the file";

            Blob reasonFile = new SerialBlob(file.getBytes());
            leave.setReasonFile(reasonFile);
            leavesRepo.save(leave);
            return "Leave added Successfully";
        }catch (IOException e) {
            e.printStackTrace();
            return "Failed to process the file";
        }
    }

    public String addLeaves(List<Leaves> leaves) {
        for (Leaves leave : leaves) {
            try {
                addLeave(leave);
            } catch (IllegalArgumentException e) {
                return "Error adding regularization: " + e.getMessage();
            }
        }
        
        return "Leaves added Successfully";
    }

    public String approve(Long leaveId) {
        Leaves leave = leavesRepo.findById(leaveId).orElseThrow(() -> new IllegalStateException("Leave does not exist for given leave id"));
        leave.setApprovalStatus("Approved");
        leavesRepo.save(leave);
        String approvalResult = attendanceService.approveLeave(leave);

        if (approvalResult.equals("Attendance modified successfully")) 
            return "Leave approved successfully";
        else 
            return "Error: " + approvalResult;
    }

    public String decline(Long leaveId, String reasonForReject) {
        Leaves leave = leavesRepo.findById(leaveId).orElseThrow(() -> new IllegalStateException("Leave does not exist for given leave id"));
        leave.setApprovalStatus("Declined");
        leave.setReasonForReject(reasonForReject);
        leavesRepo.save(leave);

        return "Leave declined successfully";
    }
}
