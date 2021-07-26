package com.tmax.eTest.Contents.repository;

import java.util.ArrayList;
import java.util.List;

import com.tmax.eTest.Common.model.problem.TestProblem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface TestProblemRepository extends  JpaRepository<TestProblem, Integer>{
	@Query(value="select t from TestProblem t where t.setNum = :setNum and t.sequence > :seq order by t.sequence asc")
	public ArrayList<TestProblem>findSetProblems(@Param("setNum") int setNum, @Param("seq") int seq);
	
	@Query("SELECT MAX(tp.setNum) FROM TestProblem tp")
	Integer findMaximumSetNum();
}