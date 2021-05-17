package com.tmax.eTest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProblemDemoRepository extends CrudRepository<ProblemDemoDAO, String>{
	
	ProblemDemoDAO findByProbUuid(String probUuid);
}
