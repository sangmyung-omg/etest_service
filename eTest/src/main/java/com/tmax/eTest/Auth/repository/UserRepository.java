package com.tmax.eTest.Auth.repository;

import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("AU-UserRepository")
public interface UserRepository extends JpaRepository<UserMaster, Long> {
    UserMaster findByUsername(String username);


}
