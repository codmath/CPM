package com.incture.cpm.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Regularize;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Repo.RegularizeRepository;
import com.incture.cpm.Repo.TalentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegularizeService {

    @Autowired
    private RegularizeRepository regularizeRepository;

    @Autowired
    private TalentRepository talentRepo;

    public List<Regularize> getAllRegularization() {
        return regularizeRepository.findAll();
    }

    public List<Regularize> getAllPendingRegularization() {
        return regularizeRepository.findByApprovalStatus("Pending");
    }
    
    public Optional<Regularize> getRegularizeById(Long regularizeId) {
        return regularizeRepository.findById(regularizeId);
    }

    public Optional<List<Regularize>> getRegularizeByTalentId(Long talentId) {
        return regularizeRepository.findByTalentId(talentId);
    }

    public String createRegularize(Regularize regularize) {
        Talent talent = talentRepo.findById(regularize.getTalentId()).orElseThrow(() -> new IllegalStateException("Talent not found for the given talentId"));

        regularize.setTalentName(talent.getTalentName());
        regularize.setApprovalStatus("Pending");
        regularize.setApprovalManager(talent.getReportingManager());

        regularizeRepository.save(regularize);
        return "saved";
    }

    public String deleteRegularize(Long regularizeId){
        regularizeRepository.findById(regularizeId).orElseThrow(() -> new IllegalStateException("Regularize not found for the given id"));

        regularizeRepository.deleteById(regularizeId);
        return "deleted";
    }

    public String declineRegularize(Long id) {
        Regularize regularize = regularizeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Regularization not found for given regularizationId"));
        regularize.setApprovalStatus("Declined");
        regularizeRepository.save(regularize);

        return "Regularization successfully declined"; 
    } 
    
    public String createRegularizeList(List<Regularize> regularizeList) {
        for (Regularize regularize : regularizeList) {
            try {
                createRegularize(regularize);
            } catch (IllegalArgumentException e) {
                return "Error adding regularization: " + e.getMessage();
            }
        }
        return "All regularizations saved successfully";
    }

}
