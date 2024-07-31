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
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getMembersOfTeam(Long teamId) {
        List<Member> memberList = memberRepository.findByTeam(teamRepository.findById(teamId).get());
        if (memberList.isEmpty()) {
            throw new ResourceNotFoundException("No Members Present in the team with Id " + teamId);
        }
        return memberList;
    }

    public Member addMember(Long talentId, Long teamId, String role) {
        Optional<Member> existingMember = memberRepository.findByTalentId(talentId);
        if (existingMember.isPresent()) {
            throw new ResourceAlreadyExistsException("Talent already exists in another team");
        }

        Optional<Mentor> mentor = mentorRepository.findByTalentId(talentId);
        if (mentor.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a Mentor : Team Member Role Could not be assigned ");
        }

        Optional<Manager> manager = managerRepository.findByTalentId(talentId);
        if (manager.isPresent()) {
            throw new ResourceAlreadyExistsException(
                    "Talent Already present as a Project Manager : Team Member Role Could not be assigned ");
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

        Member createdMember = new Member();
        createdMember.setTalentId(talentId);
        createdMember.setName(talent.get().getTalentName());
        createdMember.setEkYear(talent.get().getEkYear());
        createdMember.setSkills(talent.get().getTalentSkills());
        createdMember.setRole(role);
        createdMember.setTeam(team.get());

        teamService.incrementMemberCount(teamId);
        return memberRepository.save(createdMember);
    }

    public String deleteMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            return "No Team member is present with given MemberId";
        }
        Long teamId = member.get().getTeam().getTeamId();
        teamService.decrementMemberCount(teamId);
        memberRepository.delete(member.get());
        return "Team Member Removed Successfully";
    }

    public String updateMember(Long memberId, Member updateMember) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            return "No Team member is present with given MemberId";
        }
        updateMember.setMemberId(memberId);
        memberRepository.save(updateMember);
        return "Team Member details updated successfully";
    }
}
