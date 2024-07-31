package com.incture.cpm.Repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Entity.Projects;

public interface ProjectsRepository extends JpaRepository<Projects,Long>{

    List<Projects> findByInkathon(Inkathon inkathon);
    
}
