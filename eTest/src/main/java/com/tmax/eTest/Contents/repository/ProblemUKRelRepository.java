package com.tmax.eTest.Contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.tmax.eTest.Contents.model.ProbUKCompositeKey;
import com.tmax.eTest.Contents.model.ProblemUKRelation;

public interface ProblemUKRelRepository extends JpaRepository<ProblemUKRelation, ProbUKCompositeKey>{

}
