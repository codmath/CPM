package com.incture.cpm.Service;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.helper.Helper;
 
import com.incture.cpm.Repo.CandidateRepository;
import com.incture.cpm.Repo.CollegeRepository;
import com.incture.cpm.Util.ExcelUtil;
 
import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private CollegeRepository collegeRepository;
 
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }
 
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }
 
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }
 
    public Candidate updateCandidate(Long id, Candidate candidate) {
        candidate.setCandidateId(id);
        return candidateRepository.save(candidate);
    }
 
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
 
    // ----------------------------------------------------------------
   
   
    public void save(MultipartFile file) {
 
        try {
            List<Candidate> candidates = Helper.convertExcelToListOfProduct(file.getInputStream());
            this.candidateRepository.saveAll(candidates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public List<Candidate> getAllProducts() {
        return this.candidateRepository.findAll();
    }
 
    @Transactional
    public void feedCandidateData(MultipartFile file, int collegeId) throws NotFoundException {
        try {
            List<Map<String, String>> dataList = excelUtil.readExcelFile(file);
   
            for (Map<String, String> data : dataList) {
                Optional<CollegeTPO> clg = collegeRepository.findById(collegeId);
                if(clg.isEmpty()) throw new NotFoundException();
                String email = Optional.ofNullable(data.get("Email Id")).orElse("");
                Optional<Candidate> existingCandidate = candidateRepository.findByEmail(email);
                if (existingCandidate.isPresent()) {
                    // Skip duplicate entry
                    continue;
                }
               
            Candidate cand = new Candidate();
            cand.setCandidateName(Optional.ofNullable(data.get("Student Name")).orElse(""));
            cand.setEmail(email);
            cand.setTenthPercent(Optional.ofNullable(data.get("10th %")).map(Double::parseDouble).orElse(0.0));
            cand.setTwelthPercent(Optional.ofNullable(data.get("12th %")).map(Double::parseDouble).orElse(0.0));
            cand.setFatherName(Optional.ofNullable(data.get("Father Name")).orElse(""));
            cand.setPermanentAddress(Optional.ofNullable(data.get("address")).orElse(""));
            cand.setMotherName(Optional.ofNullable(data.get("Mother Name")).orElse(""));
            cand.setCgpaUndergrad(Optional.ofNullable(data.get("UG - CGPA")).map(Double::parseDouble).orElse(0.0));
            cand.setCgpaMasters(Optional.ofNullable(data.get("PG")).map(Double::parseDouble).orElse(0.0));
            cand.setPhoneNumber(Optional.ofNullable(data.get("Contact No")).orElse(""));
            cand.setCurrentLocation(Optional.ofNullable(data.get("Current Location")).orElse(""));
            cand.setAadhaarNumber(Optional.ofNullable(data.get("Aadhar No (Mandate)")).orElse(""));
            cand.setDOB(Optional.ofNullable(data.get("Dob")).orElse(""));
            cand.setDepartment(Optional.ofNullable(data.get("Branch")).orElse(""));
            cand.setPanNumber(Optional.ofNullable(data.get("Pan No (optional)")).orElse(""));
            cand.setAlternateNumber(Optional.ofNullable(data.get("Alternate Number")).orElse(""));
            cand.setCollegeId(collegeId);
            cand.setStatus("Interview Pending");
            cand.setEkYear(String.valueOf(Year.now().getValue()));
            cand.setCandidateCollege(clg.get().getCollegeName());
            candidateRepository.save(cand);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
   
}