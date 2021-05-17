package com.tmax.eTest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDAO, String> {
	
}
