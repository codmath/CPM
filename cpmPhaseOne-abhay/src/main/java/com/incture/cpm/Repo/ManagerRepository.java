package com.incture.cpm.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Manager;
import com.incture.cpm.Entity.Team;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    List<Manager> findByTeam(Team team);

    Optional<Manager> findByTalentId(Long talentId);

    // @Query(value = "SELECT a FROM Manager a WHERE a.managerId = :managerId AND
    // a.teamId = :teamId", nativeQuery = true)
    // Optional<Manager> findByManagerIdAndTeamId(
    // @Param("managerId") Long managerId,
    // @Param("teamId") Long teamId);

    @Query(value = "SELECT * FROM manager WHERE manager_id = :managerId AND team_id = :teamId", nativeQuery = true)
    Optional<Manager> findByManagerIdAndTeamId(
            @Param("managerId") Long managerId,
            @Param("teamId") Long teamId);
}
