package com.incture.cpm.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incture.cpm.Entity.UnauthorizedUser;

@Repository
public interface UnauthorizedUserRepo extends JpaRepository<UnauthorizedUser, Long> {

    Optional<UnauthorizedUser> findByEmail(String email);
    
}
