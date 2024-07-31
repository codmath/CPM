package com.incture.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Entity.Member;
import com.incture.cpm.Entity.Mentor;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Entity.Team;
import com.incture.cpm.Exception.ResourceAlreadyExistsException;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.ManagerRepository;
import com.incture.cpm.Repo.MemberRepository;
import com.incture.cpm.Repo.MentorRepository;
import com.incture.cpm.Repo.TalentRepository;
import com.incture.cpm.Repo.TeamRepository;

@Service
public class ManagerService {
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MentorRepository mentorRepository;

    public Optional<Manager> getManagerById(Long managerId) {
        return managerRepository.findById(managerId);
    }

    public List<Manager> getAllManager() {
        return managerRepository.findAll();
    }

    public List<Manager> getManagerOfTeam(Long teamId) {
        List<Manager> managerList = managerRepository.findByTeam(teamRepository.findById(teamId).get());
        if (managerList.isEmpty()) {
            throw new ResourceNotFoundException("No Manager Present in the team with Id " + teamId);
        }
        return managerList;
    }

    public Manager addManager(Long talentId, Long teamId) {
        Optional<Manager> existingManager = managerRepository.findByTalentId(talentId);
        if (existingManager.isPresent()) {
            throw new ResourceAlreadyExistsException("Manager already exists in another team");
        }

        Optional<Member> member = memberRepository.findByTalentId(talentId);
        if (member.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a member : Project Manager Role Could not be assigned ");
        }

        Optional<Mentor> mentor = mentorRepository.findByTalentId(talentId);
        if (mentor.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a Mentor : Project Manager Role Could not be assigned ");
        }

        Optional<Talent> talent = talentRepository.findById(talentId);
        if (talent.isEmpty()) {
            throw new ResourceNotFoundException("No Talent exists with given Talent Id");
        } else if (talent.get().getTalentStatus() != "ACTIVE") {
            throw new ResourceNotFoundException("Talent is Inactive");
        }

        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect Team Id given");
        }

        Manager createdManager = new Manager();
        createdManager.setTalentId(talentId);
        createdManager.setName(talent.get().getTalentName());
        createdManager.setTeam(team.get());
        return managerRepository.save(createdManager);
    }

    public String deleteManager(Long managerId) {
        // Optional<Manager> manager = managerRepository.findById(managerId);
        // if (manager.isEmpty()) {
        // return "No Team managar is present with given ManagerId";
        // }
        // managerRepository.delete(manager.get());
        // return "Team Manager Removed Successfully";
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isEmpty()) {
            return "No Team Manager is present with given ManagerId";
        }

        try {
            managerRepository.deleteById(managerId);
            return "Team Manager Removed Successfully 111";
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return "Error occurred while deleting the Team Manager";
        }
    }

    public String updateManager(Long managerId, Manager updateManager) {
        Optional<Manager> manager = managerRepository.findById(managerId);
        if (manager.isEmpty()) {
            return "No Team manager is present with given ManagerId";
        }
        updateManager.setManagerId(managerId);
        managerRepository.save(updateManager);
        return "Team Manager details updated successfully";
    }

    public Manager addAndUpdateManager(Long talentId, Long teamId, Long managerId) {
        // if (managerId != -1) {
        Optional<Manager> existingManager = managerRepository.findByManagerIdAndTeamId(managerId, teamId);
        if (existingManager.isPresent()) {
            @SuppressWarnings("unused")
            String msg = deleteManager(managerId);
            System.out.println(msg);
        }
        // }
        Manager manager = addManager(talentId, teamId);
        return manager;
    }
}
