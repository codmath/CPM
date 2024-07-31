package com.incture.cpm.Repo;
import com.incture.cpm.Entity.Assignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface AssignmentRepo  extends JpaRepository<Assignment, Integer> {

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE assignment MODIFY COLUMN assignment_file LONGBLOB", nativeQuery = true)
    void modifyAssignmentFileColumn();

}

