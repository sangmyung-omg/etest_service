package com.tmax.eTest.Contents.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface ProblemChoiceRepository extends JpaRepository<ProblemChoiceDAO, ProblemChoiceCompositeKey>{

		@Query(value="select c from ProblemChoiceDAO c where c.probID = :id")
		public List<ProblemChoiceDAO>findAllByProbID(@Param("id") ProblemDAO id);
}
