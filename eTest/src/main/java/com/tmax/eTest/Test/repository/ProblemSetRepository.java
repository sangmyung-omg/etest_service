package com.tmax.eTest.Test.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tmax.eTest.Test.model.ProblemSetDemo;

public interface ProblemSetRepository extends CrudRepository<ProblemSetDemo, String>{
	
	List<ProblemSetDemo> findAllByChapter(String chapter);
}
