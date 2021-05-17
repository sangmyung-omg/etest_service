package com.tmax.eTest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ProblemSetRepository extends CrudRepository<ProblemSetDemoDAO, String>{
	
	List<ProblemSetDemoDAO> findAllByChapter(String chapter);
}
