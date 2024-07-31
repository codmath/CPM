package com.incture.cpm.Service;

import com.incture.cpm.Entity.CollegeTPO;
import com.incture.cpm.Entity.InterviewerScheduling;
import com.incture.cpm.Exception.DuplicateEntryException;
import com.incture.cpm.Repo.CollegeTPORepo;
import com.incture.cpm.Repo.IntervieweScheduleRepo;
import com.incture.cpm.helper.Apiresponse;

import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class CollegeTpoService {
    @Autowired
    private CollegeTPORepo myTPORepo;

    @Autowired
    private IntervieweScheduleRepo intervieweScheduleRepo;
    public CollegeTPO insertFunction(CollegeTPO collegeTPO) {
        String collegename=collegeTPO.getCollegeName();
        CollegeTPO existingCollegeTPO=myTPORepo.findByCollegeName(collegename);
        if(existingCollegeTPO!=null){
            throw new DuplicateEntryException("");
        }
        // collegeTPO.setCollegeId(generateRandomInt());
        return this.myTPORepo.save(collegeTPO);
    }


    public List<CollegeTPO> findData() {
        return this.myTPORepo.findAll();
    }

    public CollegeTPO getCollegeTPOById(int collegeId) {
        return myTPORepo.findById(collegeId).orElse(null);
    }

    public CollegeTPO updateCollegeTPO(int collegeId, CollegeTPO collegeTPO) {
        Optional<CollegeTPO> existingCollegeTPOOptional = myTPORepo.findById(collegeId);
        if (existingCollegeTPOOptional.isPresent()) {
            CollegeTPO existingCollegeTPO = existingCollegeTPOOptional.get();
            existingCollegeTPO.setCollegeName(collegeTPO.getCollegeName());
            existingCollegeTPO.setTpoName(collegeTPO.getTpoName());
            existingCollegeTPO.setPrimaryEmail(collegeTPO.getPrimaryEmail());
            existingCollegeTPO.setPhoneNumber(collegeTPO.getPhoneNumber());
            existingCollegeTPO.setAddressLine1(collegeTPO.getAddressLine1());
            existingCollegeTPO.setAddressLine2(collegeTPO.getAddressLine2());
            existingCollegeTPO.setLocation(collegeTPO.getLocation());
            existingCollegeTPO.setRegion(collegeTPO.getRegion());
            existingCollegeTPO.setCollegeOwner(collegeTPO.getCollegeOwner());
            existingCollegeTPO.setPrimaryContact(collegeTPO.getPrimaryContact());
            existingCollegeTPO.setSecondaryContact(collegeTPO.getSecondaryContact());
            existingCollegeTPO.setTier(collegeTPO.getTier());
            existingCollegeTPO.setPinCode(collegeTPO.getPinCode());
            existingCollegeTPO.setState(collegeTPO.getState());
            existingCollegeTPO.setCompensation(collegeTPO.getCompensation());
            return myTPORepo.save(existingCollegeTPO);
        } else {
            return null; // Or throw an exception indicating that the collegeId was not found
        }

    }

    public ResponseEntity<Apiresponse> deleteCollegeTPO(int collegeId) {
        CollegeTPO existingaCollegeTpo=myTPORepo.findById(collegeId).orElse(null);
 
        InterviewerScheduling existinginterviewerScheduling=existingaCollegeTpo.getInterviewerSchedulings();
            if (existinginterviewerScheduling != null) {
                existinginterviewerScheduling.setCollegeTPO(null);
                intervieweScheduleRepo.save(existinginterviewerScheduling);
                                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new Apiresponse("Interviewer scheduling exists and has been unlinked."));

            }

                myTPORepo.deleteById(collegeId);
                return ResponseEntity.ok(new Apiresponse("College TPO deleted successfully."));

            }
/* 
    private int generateRandomInt() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }
 */
}
