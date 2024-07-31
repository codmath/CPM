package com.incture.cpm.Repo;

import com.incture.cpm.Entity.CollegeTPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollegeTPORepo extends JpaRepository<CollegeTPO, Integer> {

    CollegeTPO findByCollegeName(String collegename);

//    void deleteEntitiesByIdIn(List<Integer> collegeId);
// ALTER TABLE CollegeTPO MODIFY COLUMN college_id INT AUTO_INCREMENT;

}
