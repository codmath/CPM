package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Team;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // request for getting teams of a particular inkathon
    @GetMapping("/getInkathonTeams/{inkathon_id}")
    public ResponseEntity<List<Team>> getTeamByInkathonId(@PathVariable("inkathon_id") Long inkathonId) {
        try {
            List<Team> team = teamService.getTeamsByInkathonId(inkathonId);
            return ResponseEntity.ok(team);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Long id) {
        try {
            Optional<Team> team = teamService.getTeamById(id);
            return team.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create/{inkathon_id}/{project_id}")
    public ResponseEntity<Team> createTeam(@PathVariable("inkathon_id") Long inkathonId,
            @PathVariable("project_id") Long projectId, @RequestBody Team team) {
        try {
            Team createdTeam = teamService.createTeam(inkathonId, projectId, team);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/{project_id}")
    public ResponseEntity<Team> updateTeam(@PathVariable("id") Long id, @PathVariable("project_id") Long projectId,
            @RequestBody Team team) {
        try {
            Team updatedTeam = teamService.updateTeam(id, projectId, team);
            return ResponseEntity.ok(updatedTeam);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // *********************************************

    @PutMapping("/uploadPresentation/{teamId}")
    public ResponseEntity<Team> uploadPresentationFile(@RequestPart MultipartFile presentationFile,
            @PathVariable("teamId") Long teamId) throws SerialException, SQLException, IOException {
        Blob pptFile = new SerialBlob(presentationFile.getBytes());
        Team team = teamService.getTeamById(teamId).get();
        team.setPresentationFile(pptFile);
        Team updatedTeam = teamService.updateTeamForPresentationFileOrProgress(teamId, team);
        if (updatedTeam != null) {
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/viewpresentationfile/{teamId}")
    public ResponseEntity<byte[]> getpresentation(@PathVariable Long teamId) throws IOException {
        // Retrieve Talent object from the service layer
        Optional<Team> team = teamService.getTeamById(teamId);

        if (team.get() != null && team.get().getPresentationFile() != null) {
            try {
                // Retrieve the PDF data from the Talent object
                Blob pptBlob = team.get().getPresentationFile();
                byte[] pptData = pptBlob.getBytes(1, (int) pptBlob.length());

                // Set appropriate response headers for PDF content
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(
                        ContentDisposition.builder("inline").filename("projectDescription.pdf").build());

                return new ResponseEntity<>(pptData, headers, HttpStatus.OK);
            } catch (SQLException e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateProgress/{teamId}")
    public ResponseEntity<?> updateProgress(@PathVariable Long teamId, @RequestParam int progress) {
        try {
            Optional<Team> team = teamService.getTeamById(teamId);
            if (team.isPresent()) {
                if (progress > 100) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Progress should be less than or equal to 100");
                }
                team.get().setProgress(progress);
                Team updatedTeam = teamService.updateTeamForPresentationFileOrProgress(teamId, team.get());
                if (updatedTeam != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(updatedTeam);
                }
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
