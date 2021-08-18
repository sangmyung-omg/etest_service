package com.tmax.eTest.TestStudio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProbUKCompositeKey;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;

public interface ProbUKRelRepositoryTs extends JpaRepository<ProblemUKRelation, ProbUKCompositeKey> {
	
	public List<ProblemUKRelation> findAllByProbID_ProbIDIs(Integer probID);
	
	@EntityGraph(attributePaths = {"ukId"}, type = EntityGraph.EntityGraphType.FETCH)
	public List<ProblemUKRelation> findUKRANDUKByProbID_ProbIDIs(Integer probID);
	
}
