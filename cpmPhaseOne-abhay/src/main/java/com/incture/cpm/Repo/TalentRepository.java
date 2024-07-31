package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Talent;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Talent findByCandidateId(Long candidateId);

    List<Talent> findByReportingManager(String reportingManager);

    Optional<Talent> findByEmail(String email);

    // These all are for analytics of inactive employees/talents

    @Query(value = "Select count(talent_id) from talent", nativeQuery = true)
    long countTotalTalents();

    @Query(value = "Select count(talent_id) from talent where talent_status='ACTIVE'", nativeQuery = true)
    long countActiveTalents();

    @Query(value = "Select count(talent_id) from talent where talent_status<>'ACTIVE'", nativeQuery = true)
    long countInactiveTalents();

    @Query(value = "Select count(talent_id) from talent where talent_status='DECLINED'", nativeQuery = true)
    long countDeclinedTalents();

    @Query(value = "Select count(talent_id) from talent where talent_status='RESIGNED'", nativeQuery = true)
    long countResignedTalents();

    @Query(value = "Select count(talent_id) from talent where talent_status='REVOKED'", nativeQuery = true)
    long countRevokedTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Better Offer%' ", nativeQuery = true)
    long countBetterOfferTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Pursuing Higher Studies%' ", nativeQuery = true)
    long countHigherStudiesTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Family Reasons%' ", nativeQuery = true)
    long countFamilyReasonsTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Health Reasons%' ", nativeQuery = true)
    long countHealthReasonsTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Performance Issues%' ", nativeQuery = true)
    long countPerformanceIssuesTalents();

    @Query(value = "Select count(talent_id) from talent where exit_reason LIKE '%Others%' ", nativeQuery = true)
    long countOtherReasonTalents();
}
