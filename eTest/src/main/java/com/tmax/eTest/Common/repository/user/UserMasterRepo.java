package com.tmax.eTest.Common.repository.user;

import com.tmax.eTest.Common.model.user.UserMaster;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMasterRepo extends JpaRepository<UserMaster, String>{
    
}
