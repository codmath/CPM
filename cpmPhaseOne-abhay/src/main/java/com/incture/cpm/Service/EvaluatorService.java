package com.incture.cpm.Service;

import com.incture.cpm.Entity.Evaluator;
import com.incture.cpm.Repo.EvaluatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluatorService {
    @Autowired
    private EvaluatorRepo evaluatorRepo;
    public Evaluator inserting(Evaluator evaluator) {
        return evaluatorRepo.save(evaluator);
    }

    public List<Evaluator> getall() {
        return evaluatorRepo.findAll();
    }
}
