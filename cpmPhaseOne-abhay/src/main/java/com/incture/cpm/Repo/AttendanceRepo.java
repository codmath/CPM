package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Attendance;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long>{
    Optional<Attendance> findByTalentIdAndDate(Long talentId, String date);
    Optional<List<Attendance>> findByDate(String date);

    Optional<List<Attendance>> findByTalentId(Long talentId);

    Optional<Attendance> findByDateAndTalentId(String date, Long talentId);

    @Query("SELECT a FROM Attendance a WHERE a.date BETWEEN :startDate AND :endDate AND a.talentId = :talentId")
    Optional<List<Attendance>> findByDateRangeAndTalent(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("talentId") Long talentId);
}
