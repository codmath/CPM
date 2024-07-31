package com.incture.cpm.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Dto.TalentSummaryDto;
import com.incture.cpm.Entity.Candidate;
import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Entity.Member;
import com.incture.cpm.Entity.Mentor;
import com.incture.cpm.Entity.Talent;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Repo.ManagerRepository;
import com.incture.cpm.Repo.MemberRepository;
import com.incture.cpm.Repo.MentorRepository;
import com.incture.cpm.Repo.TalentRepository;

@Service
public class TalentService {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MentorService mentorService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ManagerRepository managerRepository;

    public Talent addTalentFromCandidate(Candidate candidate) {
        Talent existingtTalent = talentRepository.findByCandidateId(candidate.getCandidateId());
        if (existingtTalent != null) {
            return null;
        }
        Talent newTalent = new Talent();
        newTalent.setCandidateId(candidate.getCandidateId());
        newTalent.setTalentName(candidate.getCandidateName());
        newTalent.setCollegeName(candidate.getCandidateCollege());
        newTalent.setDepartment(candidate.getDepartment());
        newTalent.setEmail(candidate.getEmail());
        newTalent.setPhoneNumber(candidate.getPhoneNumber());
        newTalent.setAlternateNumber(candidate.getAlternateNumber());
        newTalent.setTenthPercent(candidate.getTenthPercent());
        newTalent.setTwelthPercent(candidate.getTwelthPercent());
        newTalent.setCurrentLocation(candidate.getCurrentLocation());
        newTalent.setPermanentAddress(candidate.getPermanentAddress());
        newTalent.setPanNumber(candidate.getPanNumber());
        newTalent.setAadhaarNumber(candidate.getAadhaarNumber());
        newTalent.setFatherName(candidate.getFatherName());
        newTalent.setMotherName(candidate.getMotherName());
        newTalent.setDob(candidate.getDOB());
        newTalent.setCgpaUndergrad(candidate.getCgpaUndergrad());
        newTalent.setCgpaMasters(candidate.getCgpaMasters());
        return talentRepository.save(newTalent);
    }

    public Talent createTalent(Talent talent) {
        return talentRepository.save(talent);
    }

    public List<Talent> getAllTalents() {
        return talentRepository.findAll();
    }

    public Talent getTalentById(Long talentId) {
        return talentRepository.findById(talentId).orElse(null);
    }

    public Talent updateTalent(Talent talent, Long talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);

        if (existingTalent != null) {
            talent.setTalentId(talentId);
            talent.setCandidateId(existingTalent.getCandidateId());
            return talentRepository.save(talent);
        }
        return null;
    }

    public boolean deleteTalent(Long talentId) {
        Talent existingTalent = talentRepository.findById(talentId).orElse(null);
        if (existingTalent != null) {
            talentRepository.deleteById(talentId);
            return true;
        }
        return false;
    }

    // new added functionality

    // This functionality is for resigned employees
    public Talent resignTalent(Long talentId, String talentStatus, String exitReason, String exitDate,
            String exitComment) {
        Talent talent = talentRepository.findById(talentId).orElse(null);
        if (talent == null) {
            throw new ResourceNotFoundException("No Talent exists with given Talent Id");
        }
        if (talentStatus == "ACTIVE") {
            talent.setTalentStatus(talentStatus);
            talent.setExitReason("NA");
            talent.setExitDate("NA");
            talent.setExitComment("NA");
        } else {
            talent.setTalentStatus(talentStatus);
            talent.setExitReason(exitReason);
            talent.setExitDate(exitDate);
            talent.setExitComment(exitComment);

            // remove the talent from the team(whether Project Manager, Mentor, Member of
            // the team ) when he will be inactive;

            Optional<Member> member = memberRepository.findByTalentId(talentId);
            if (member.isPresent()) {
                memberService.deleteMember(member.get().getMemberId());
            }

            Optional<Mentor> mentor = mentorRepository.findByTalentId(talentId);
            if (mentor.isPresent()) {
                mentorService.deleteMentor(mentor.get().getMentorId());
            }

            Optional<Manager> manager = managerRepository.findByTalentId(talentId);
            if (manager.isPresent()) {
                managerService.deleteManager(manager.get().getManagerId());
            }

        }
        return talentRepository.save(talent);
    }

    // FOR STATS OF TALENT TABLE

    public TalentSummaryDto talentStats() {
        long totalTalents = talentRepository.countTotalTalents();
        long activeTalents = talentRepository.countActiveTalents();
        long inactiveTalents = talentRepository.countInactiveTalents();
        long declinedTalents = talentRepository.countDeclinedTalents();
        long resignedTalents = talentRepository.countResignedTalents();
        long revokedTalents = talentRepository.countRevokedTalents();
        long talentLeftForBetterOffer = talentRepository.countBetterOfferTalents();
        long talentLeftForHigherStudies = talentRepository.countHigherStudiesTalents();
        long talentLeftForFamilyReasons = talentRepository.countFamilyReasonsTalents();
        long talentLeftForHealthReasons = talentRepository.countHealthReasonsTalents();
        long talentLeftForPerformanceIssues = talentRepository.countPerformanceIssuesTalents();
        long talentLeftForOthers = talentRepository.countOtherReasonTalents();

        TalentSummaryDto result = new TalentSummaryDto(totalTalents, activeTalents, inactiveTalents, declinedTalents,
                resignedTalents, revokedTalents, talentLeftForBetterOffer, talentLeftForHigherStudies,
                talentLeftForFamilyReasons,
                talentLeftForHealthReasons, talentLeftForPerformanceIssues, talentLeftForOthers);
        return result;
    }

}