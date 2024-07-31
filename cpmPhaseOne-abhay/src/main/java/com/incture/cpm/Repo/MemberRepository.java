package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Member;
import com.incture.cpm.Entity.Team;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByTalentId(Long talentId);

    List<Member> findByTeam(Team team);
}
