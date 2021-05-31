package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Test.model.ProblemDemo;

public interface ProblemDemoRepository extends CrudRepository<ProblemDemo, String>{
	
	ProblemDemo findByProbUuid(String probUuid);
}
