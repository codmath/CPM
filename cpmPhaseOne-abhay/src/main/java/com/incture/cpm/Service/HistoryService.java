package com.incture.cpm.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incture.cpm.Entity.History;
import com.incture.cpm.Repo.HistoryRepo;

@Service
public class HistoryService {
    @Autowired
    HistoryRepo historyRepo;

    public void logHistory(String entityId, String entityType, String logEntry, String userName) {
        History historyEntry = new History();
        historyEntry.setEntityId(entityId);
        historyEntry.setEntityType(entityType);
        historyEntry.setLogEntry(logEntry);
        historyEntry.setUserName(userName);

        historyRepo.save(historyEntry);
    }

    public List<History> getAllHistoryByEntityId(String entityId, String entityType) {
        return historyRepo.findByEntityIdAndEntityType(entityId, entityType);
    }

    public List<History> getAllUserHistory(String entityType) {
        return historyRepo.findAllByEntityType(entityType); 
    }
}
