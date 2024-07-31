package com.incture.cpm.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Entity.Team;

public interface TeamRepository extends JpaRepository<Team,Long>{
    
    List<Team> findByInkathon(Inkathon inkathon);
}
