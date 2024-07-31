package com.incture.cpm.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Exception.ResourceAlreadyExistsException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.ManagerService;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @GetMapping("/getbyId/{managerId}")
    public ResponseEntity<?> getManagerById(@PathVariable Long managerId) {
        try {
            Optional<Manager> manager = managerService.getManagerById(managerId);
            if (manager.isEmpty()) {
                throw new ResourceNotFoundException("No Manager found with the given ID");
            }
            return ResponseEntity.ok(manager);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching manager: " + ex.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllManagers() {
        try {
            List<Manager> managers = managerService.getAllManager();
            return ResponseEntity.ok(managers);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching managers: " + ex.getMessage());
        }
    }

    @GetMapping("/getTeamManager/{teamId}")
    public ResponseEntity<?> getManagersOfTeam(@PathVariable Long teamId) {
        try {
            List<Manager> managers = managerService.getManagerOfTeam(teamId);
            return ResponseEntity.ok(managers);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching managers of team: " + ex.getMessage());
        }
    }

    @PostMapping("/addManager")
    public ResponseEntity<?> addManager(@RequestParam Long talentId, @RequestParam Long teamId,
            @RequestParam Long managerId) {
        try {
            Manager manager = managerService.addAndUpdateManager(talentId, teamId, managerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(manager);
        } catch (ResourceAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding manager: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{managerId}")
    public ResponseEntity<?> deleteManager(@PathVariable Long managerId) {
        try {
            String message = managerService.deleteManager(managerId);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting manager: " + ex.getMessage());
        }
    }

    @PutMapping("/update/{managerId}")
    public ResponseEntity<?> updateManager(@PathVariable Long managerId, @RequestBody Manager updateManager) {
        try {
            String message = managerService.updateManager(managerId, updateManager);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating manager: " + ex.getMessage());
        }
    }
}
