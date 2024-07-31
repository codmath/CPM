package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.incture.cpm.Entity.TalentAssessment;

public interface TalentAssessmentRepository extends JpaRepository<TalentAssessment, Long> {
    Optional<List<TalentAssessment>> findAllByTalentId(Long talentId);

    @Query("SELECT a FROM TalentAssessment a WHERE a.assessmentId = :assessmentId AND a.talentId = :talentId")
    Optional<TalentAssessment> findByAssessmentIdAndTalentId(
            @Param("assessmentId") Long assessmentId,
            @Param("talentId") Long talentId);

    List<TalentAssessment> findAllByAssessmentId(Long assessmentId);
}
