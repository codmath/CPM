package com.incture.cpm.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.History;

@Repository
public interface HistoryRepo extends JpaRepository<History, Long> {
    List<History> findByEntityIdAndEntityType(String entityId, String entityType);

    List<History> findAllByEntityType(String entityType);
}
