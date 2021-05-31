package com.tmax.eTest.Contents.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProblemRepository extends JpaRepository<ProblemDAO, Long>{

}
