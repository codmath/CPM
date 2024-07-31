package com.incture.cpm.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incture.cpm.Entity.Inkathon;
import com.incture.cpm.Service.InkathonService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/inkathons")
public class InkathonController {

    @Autowired
    private InkathonService incathonService;

    @GetMapping
    public List<Inkathon> getAllIncathons() {
        return incathonService.getAllIncathons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inkathon> getIncathonById(@PathVariable("id") Long id) {
        Optional<Inkathon> incathon = incathonService.getIncathonById(id);
        return incathon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/create")
    public ResponseEntity<Inkathon> createIncathon(@RequestParam("inkathonName") String name,@RequestParam("inkathonDesc") String description) {
        Inkathon createdIncathon = incathonService.createInkathon(name,description);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncathon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inkathon> updateIncathon(
            @PathVariable("id") Long id,
            @RequestBody Inkathon updatedIncathon) {
        Inkathon updated = incathonService.updateIncathon(id, updatedIncathon);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIncathon(@PathVariable("id") Long id) {
        incathonService.deleteIncathon(id);
        return ResponseEntity.noContent().build();
    }
}
