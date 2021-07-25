package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Test.model.UserMaster;

public interface UserRepository extends CrudRepository<UserMaster, String> {

    UserMaster findByEmail(String username);
}
