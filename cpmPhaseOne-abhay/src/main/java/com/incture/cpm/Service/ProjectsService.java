package com.incture.cpm.Service;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Entity.Projects;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.InkathonRepository;
import com.incture.cpm.Repo.ProjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class ProjectsService {

    @Autowired
    private  ProjectsRepository projectsRepository;

    @Autowired
    InkathonRepository inkathonRepository;

    public List<Projects> getAllProjects() {
        return projectsRepository.findAll();
    }

    //method for showing projects under a particular Inkathon
    public List<Projects> getProjectsByInkathonId(Long inkathonId){
        List<Projects> projectList=projectsRepository.findByInkathon(inkathonRepository.findById(inkathonId).get());
        if (projectList.isEmpty()) {
            throw new ResourceNotFoundException("Projects with IncathonId " + inkathonId + " not found");
        }
        return projectList;
    }
    //*********************************************************** */

    public Optional<Projects> getProjectById(Long id) {
        Optional<Projects> project = projectsRepository.findById(id);
        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Project with id " + id + " not found");
        }
        return project;
    }

    public Projects createProject(Long inkathonId,Projects project) throws IOException {
        Optional<Inkathon> inkathon=inkathonRepository.findById(inkathonId);
        if(inkathon.isEmpty()){
            throw new ResourceNotFoundException("Inkathon with id " + inkathonId + " not found");
        }
        project.setInkathon(inkathon.get());
        return projectsRepository.save(project);
    }


    public Optional<Projects> updateProject(Long projectId, String projectTitle, String projectDescription,
            MultipartFile descriptionFile) throws SQLException, IOException {
        return projectsRepository.findById(projectId).map(project -> {
            project.setProjectTitle(projectTitle);
            project.setProjectDescription(projectDescription);
            if (descriptionFile != null && !descriptionFile.isEmpty()) {
                try {
                    Blob blob = new SerialBlob(descriptionFile.getBytes());
                    project.setDescriptionFile(blob);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
            return projectsRepository.save(project);
        });
    }

    public void deleteProject(Long id) {
        Optional<Projects> existingProject = projectsRepository.findById(id);
        if (existingProject.isEmpty()) {
            throw new ResourceNotFoundException("Project with id " + id + " not found");
        }
        projectsRepository.deleteById(id);
    }
}
