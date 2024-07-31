package com.incture.cpm.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public Optional<Mentor> getMentorById(Long mentorId) {
        return mentorRepository.findById(mentorId);
    }

    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    public List<Mentor> getMentorsOfTeam(Long teamId) {
        List<Mentor> mentorList = mentorRepository.findByTeam(teamRepository.findById(teamId).get());
        if (mentorList.isEmpty()) {
            throw new ResourceNotFoundException("No Mentors Present in the team with Id " + teamId);
        }
        return mentorList;
    }

    public Mentor addMentor(Long talentId, Long teamId) {
        Optional<Talent> talent = talentRepository.findById(talentId);
        if (talent.isEmpty()) {
            throw new ResourceNotFoundException("No Talent exist with given Talent Id");
        } else if (talent.get().getTalentStatus() != "ACTIVE") {
            throw new ResourceNotFoundException("Talent is Inactive");
        }

        Optional<Member> member = memberRepository.findByTalentId(talentId);
        if (member.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a member : Mentor Role Could not be assigned ");
        }

        Optional<Manager> manager = managerRepository.findByTalentId(talentId);
        if (manager.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a Project Manager : Mentor Role Could not be assigned ");
        }

        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isEmpty()) {
            throw new ResourceNotFoundException("Incorrect Team Id given");
        }
        // Create the Mentor
        Mentor createdMentor = new Mentor();
        createdMentor.setTalentId(talentId);
        createdMentor.setName(talent.get().getTalentName());
        createdMentor.setSkills(talent.get().getTalentSkills());

        // Add the Team to the Mentor
        Set<Team> teams = new HashSet<>();
        teams.add(team.get());
        createdMentor.setTeam(teams);

        // Save the Mentor to get its ID
        Mentor response = mentorRepository.save(createdMentor);

        // Add the Mentor to the Team
        Set<Mentor> mentors = team.get().getMentor();
        if (mentors == null) {
            mentors = new HashSet<>();
        }
        mentors.add(createdMentor);
        team.get().setMentor(mentors);

        // Save the Team to update the relationship
        teamRepository.save(team.get());

        return response;
    }

    public String deleteMentor(Long mentorId) {
        Optional<Mentor> mentorOptional = mentorRepository.findById(mentorId);
        if (mentorOptional.isEmpty()) {
            return "No Team mentor is present with given MentorId";
        }

        Mentor mentor = mentorOptional.get();
        Set<Team> teams = mentor.getTeam();

        for (Team team : teams) {
            team.getMentor().remove(mentor);
            teamRepository.save(team);
        }

        mentorRepository.delete(mentor);
        return "Team Mentor Removed Successfully";
    }

    public String updateMentor(Long mentorId, Mentor updateMentor) {
        Optional<Mentor> mentor = mentorRepository.findById(mentorId);
        if (mentor.isEmpty()) {
            return "No Team mentor is present with given MentorId";
        }
        updateMentor.setMentorId(mentorId);
        mentorRepository.save(updateMentor);
        return "Team Mentor details updated successfully";
    }
}
