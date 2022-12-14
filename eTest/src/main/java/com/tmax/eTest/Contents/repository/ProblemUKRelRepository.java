package com.tmax.eTest.Contents.repository;

import com.tmax.eTest.Common.model.uk.ProbUKCompositeKey;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("CO-ProblemUKRelRepository")

public interface ProblemUKRelRepository extends JpaRepository<ProblemUKRelation, ProbUKCompositeKey>{

}
