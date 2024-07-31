package com.incture.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Performance;

@Repository
public interface PerformanceRepo extends JpaRepository<Performance, Long>{

}
