package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Member;
import com.incture.cpm.Exception.ResourceNotFoundException;
import com.incture.cpm.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMemberById(@PathVariable Long memberId) {
        try {
            Optional<Member> member = memberService.getMemberById(memberId);
            return ResponseEntity.ok(
                    member.orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + memberId)));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getMembersOfTeam(@PathVariable Long teamId) {
        try {
            List<Member> members = memberService.getMembersOfTeam(teamId);
            return ResponseEntity.ok(members);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMember(@RequestParam Long talentId,
            @RequestParam Long teamId,
            @RequestParam String role) {
        try {
            Member response = memberService.addMember(talentId, teamId, role);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding member: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        try {
            String message = memberService.deleteMember(memberId);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting member: " + ex.getMessage());
        }
    }

    @PutMapping("/update/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable Long memberId,
            @RequestBody Member updateMember) {
        try {
            String message = memberService.updateMember(memberId, updateMember);
            return ResponseEntity.ok(message);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating member: " + ex.getMessage());
        }
    }

    @PutMapping("/updatePerformance/{memberId}")
    public ResponseEntity<String> givePerformanceRating(@PathVariable Long memberId, @RequestParam int performance) {
        try {
            Optional<Member> member = memberService.getMemberById(memberId);
            if (member.isPresent()) {
                if (performance > 5) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Progress should be in the range 1 to 5");
                }
                member.get().setPerformanceRating(performance);
                String msg = memberService.updateMember(memberId, member.get());
                if (msg != null) {
                    return ResponseEntity.status(HttpStatus.OK).body(msg);
                }
            }
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>("Error Updating Performance Rating", HttpStatus.BAD_REQUEST);
    }

}
