package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Mentor;
import com.incture.cpm.Entity.Team;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    List<Mentor> findByTeam(Team team);

    Optional<Mentor> findByTalentId(Long talentId);

}
