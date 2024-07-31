package com.incture.cpm.Repo;


import com.incture.cpm.Entity.AcademicInterns;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicInternsRepository extends JpaRepository<AcademicInterns, Long> {
    // Optional<List<AcademicInterns>> findByDateAndStatus(String date, String status);
     // You can add custom query methods if needed
    Optional<List<AcademicInterns>> findByDate(String date);
}