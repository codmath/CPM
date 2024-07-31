package com.incture.cpm.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.Leaves;

@Repository
public interface LeavesRepo extends JpaRepository<Leaves, Long>{
    
}
