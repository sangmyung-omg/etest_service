package com.tmax.eTest.Contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.tmax.eTest.Contents.model.TestProblem;

public interface TestProblemRepository extends  JpaRepository<TestProblem, Long>{

}
