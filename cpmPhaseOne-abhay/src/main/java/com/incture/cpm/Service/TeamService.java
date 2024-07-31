package com.incture.cpm.Service;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Entity.Projects;
import com.incture.cpm.Entity.Team;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.InkathonRepository;
import com.incture.cpm.Repo.ProjectsRepository;
import com.incture.cpm.Repo.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private InkathonRepository inkathonRepository;

    @Autowired
    private ProjectsRepository projectsRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // method for showing teams under a particular Inkathon
    public List<Team> getTeamsByInkathonId(Long inkathonId) {
        List<Team> teamList = teamRepository.findByInkathon(inkathonRepository.findById(inkathonId).get());
        if (teamList.isEmpty()) {
            throw new ResourceNotFoundException("Projects with IncathonId " + inkathonId + " not found");
        }
        return teamList;
    }
    // *********************************************************** */

    public Optional<Team> getTeamById(Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Team with id " + id + " not found");
        }
        return team;
    }

    public Team createTeam(Long inkathonId, Long projectId, Team team) throws IOException {
        Optional<Inkathon> inkathon = inkathonRepository.findById(inkathonId);
        Optional<Projects> project = projectsRepository.findById(projectId);
        if (inkathon.isEmpty() || project.isEmpty()) {
            throw new ResourceNotFoundException("Inkathon with id " + inkathonId + " not found");
        }
        team.setInkathon(inkathon.get());
        team.setProjects(project.get());
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Long projectId, Team updatedTeam) {
        Optional<Team> existingTeamOptional = teamRepository.findById(id);
        Optional<Projects> projectOptional = projectsRepository.findById(projectId);

        if (existingTeamOptional.isEmpty()) {
            throw new ResourceNotFoundException("Team with id " + id + " not found");
        }

        Team existingTeam = existingTeamOptional.get();

        // Update the fields of the existing team
        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setMembersCount(updatedTeam.getMembersCount());
        existingTeam.setProgress(updatedTeam.getProgress());
        existingTeam.setPresentationFile(updatedTeam.getPresentationFile());

        // Update the project if provided
        if (projectOptional.isPresent()) {
            existingTeam.setProjects(projectOptional.get());
        }

        return teamRepository.save(existingTeam);
    }

    public Team updateTeamForPresentationFileOrProgress(Long id, Team team) {
        Optional<Team> existingTeam = teamRepository.findById(id);
        if (existingTeam.isEmpty()) {
            throw new ResourceNotFoundException("Team with id " + id + " not found");
        }
        team.setTeamId(id);
        return teamRepository.save(team);
    }

    public void deleteTeam(Long id) {
        Optional<Team> existingTeam = teamRepository.findById(id);
        if (existingTeam.isEmpty()) {
            throw new ResourceNotFoundException("Team with id " + id + " not found");
        }
        teamRepository.deleteById(id);
    }

    // ***************** */

    public void incrementMemberCount(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        Team incrementTeam = team.get();
        incrementTeam.setMembersCount(incrementTeam.getMembersCount() + 1);
        incrementTeam.setTeamId(teamId);
        teamRepository.save(incrementTeam);
    }

    public void decrementMemberCount(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        Team incrementTeam = team.get();
        incrementTeam.setMembersCount(incrementTeam.getMembersCount() - 1);
        incrementTeam.setTeamId(teamId);
        teamRepository.save(incrementTeam);
    }

}
