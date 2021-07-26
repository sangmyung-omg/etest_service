package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Test.model.UserMaster;

public interface UserRepository extends JpaRepository<UserMaster, Long> {
    UserMaster findByEmail(String email);


}
