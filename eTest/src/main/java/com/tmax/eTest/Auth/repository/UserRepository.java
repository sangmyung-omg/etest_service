package com.tmax.eTest.Auth.repository;

import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("AU-UserRepository")
public interface UserRepository extends JpaRepository<UserMaster, Long> {
    Optional<UserMaster> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<UserMaster> findByUserUuid(String userUuid);
}
