package com.incture.cpm.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.cpm.Entity.Performance;
import com.incture.cpm.Exception.BadRequestException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.PerformanceService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cpm/performance")
public class PerformanceController {
    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/getAllPerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Performance> getAllPerformances() {
        return performanceService.getAllPerformances();
    }
    
    @GetMapping("/getPerformanceById")
    public ResponseEntity<Performance> getPerformanceById(@RequestParam Long talentId) {
        if (talentId == null) {
            throw new BadRequestException("Talent ID is required.");
        }
        try {
            Performance performance = performanceService.getPerformanceById(talentId);
            if (performance == null) {
                throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
            }
            return new ResponseEntity<>(performance, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updatePerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updatePerformance(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            String message = performanceService.updatePerformance(performance);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPerformanceByList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addPerformanceByList(@RequestBody List<Performance> performanceList) {
        if (performanceList == null || performanceList.isEmpty()) {
            throw new BadRequestException("Performance list is required.");
        }
        try {
            String message = performanceService.addPerformanceByList(performanceList);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateFeedback")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateFeedback(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            performanceService.updateFeedback(performance);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePerformance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePerformance(@RequestBody Performance performance) {
        if (performance == null) {
            throw new BadRequestException("Performance data is required.");
        }
        try {
            performanceService.deletePerformance(performance);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePerformanceById")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePerformance(@RequestParam Long talentId) {
        if (talentId == null) {
            throw new BadRequestException("Talent ID is required.");
        }
        try {
            performanceService.deletePerformance(talentId);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Performance not found for this id: " + talentId);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // @PostMapping("/updateFeedback")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // public ResponseEntity<String> updateFeedback(@RequestBody Performance performance) {
    //     if (performance == null) {
    //         throw new BadRequestException("Performance data is required.");
    //     }
    //     try {
    //         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //         performanceService.updateFeedback(performance, authentication.getName());
    //         return new ResponseEntity<>("OK", HttpStatus.OK);
    //     } catch (ResourceNotFoundException ex) {
    //         throw new ResourceNotFoundException("Performance not found for this id: " + performance.getTalentId());
    //     } catch (Exception ex) {
    //         return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
}
