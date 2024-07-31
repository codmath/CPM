package com.incture.cpm.Controller;

import com.incture.cpm.Entity.Evaluator;
import com.incture.cpm.Service.EvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/evaluator")
@CrossOrigin("*")
public class EvaluatorController {
    @Autowired
    private EvaluatorService evaluatorService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Evaluator> insertingFunction(@RequestBody Evaluator evaluator){
        return ResponseEntity.ok(evaluatorService.inserting(evaluator));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getallevaluator")
    public List<Evaluator> getFunction(){
        return evaluatorService.getall();
    }
}
