package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Common.model.user.UserMaster;

public interface UserRepository extends CrudRepository<UserMaster, String> {
	
}
