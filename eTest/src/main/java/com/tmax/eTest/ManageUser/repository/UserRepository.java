package com.tmax.eTest.ManageUser.repository;

import com.tmax.eTest.Common.model.user.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserMaster, String> {
    public Optional<UserMaster> findByUserUuid(String user_uuid);
}
