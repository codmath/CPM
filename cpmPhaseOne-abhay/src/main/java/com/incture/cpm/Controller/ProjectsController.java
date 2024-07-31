package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Projects;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.ProjectsService;
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

@RestController
@CrossOrigin("*")
@RequestMapping("/api/projects")
public class ProjectsController {

    @Autowired
    private ProjectsService projectsService;

    @GetMapping
    public ResponseEntity<List<Projects>> getAllProjects() {
        List<Projects> projects = projectsService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // request for getting projects of a particular inkathon
    @GetMapping("/getInkathonProject/{inkathon_id}")
    public ResponseEntity<List<Projects>> getProjectsByInkathonId(@PathVariable("inkathon_id") Long inkathonId) {
        try {
            List<Projects> projects = projectsService.getProjectsByInkathonId(inkathonId);
            return ResponseEntity.ok(projects);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projects> getProjectById(@PathVariable("id") Long id) {
        try {
            Optional<Projects> project = projectsService.getProjectById(id);
            return project.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/create/{inkathon_id}")
    public ResponseEntity<Projects> createProject(
            @PathVariable("inkathon_id") Long inkathonId,
            @RequestParam("projectTitle") String projectTitle, @RequestParam("projectDescription") String projectDescription, 
            @RequestParam("projectFile") MultipartFile projectDescriptionFile) {
        try {
            Projects project = new Projects();
            project.setProjectTitle(projectTitle);
            project.setProjectDescription(projectDescription);
            Blob projectDesc = new SerialBlob(projectDescriptionFile.getBytes());
            project.setDescriptionFile(projectDesc);

            Projects createdProject = projectsService.createProject(inkathonId, project);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Projects> updateProject(
            @PathVariable Long id,
            @RequestParam("projectTitle") String projectTitle,
            @RequestParam("projectDescription") String projectDescription,
            @RequestParam(value = "projectFile", required = false) MultipartFile descriptionFile) {
        try {
            Optional<Projects> updatedProject = projectsService.updateProject(id, projectTitle, projectDescription,
                    descriptionFile);
            if (updatedProject.isPresent()) {
                return ResponseEntity.ok(updatedProject.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException | IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
        try {
            projectsService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/viewprojectfile/{projectId}")
    public ResponseEntity<byte[]> getdescriptionFile(@PathVariable Long projectId) throws IOException {
        // Retrieve Talent object from the service layer
        Optional<Projects> project = projectsService.getProjectById(projectId);

        if (project.get() != null && project.get().getDescriptionFile() != null) {
            try {
                // Retrieve the PDF data from the Talent object
                Blob descriptionBlob = project.get().getDescriptionFile();
                byte[] pdfData = descriptionBlob.getBytes(1, (int) descriptionBlob.length());

                // Set appropriate response headers for PDF content
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDisposition(ContentDisposition.builder("inline").filename("projectDescription.pdf").build());

                return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
            } catch (SQLException e) {
                // Handle exceptions appropriately
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
