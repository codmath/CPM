package com.incture.cpm.Repo;

import com.incture.cpm.Entity.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EvaluatorRepo extends JpaRepository<Evaluator, Long> {
}
