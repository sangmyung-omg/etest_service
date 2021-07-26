package com.tmax.eTest.Contents.repository;

import java.util.List;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.ProblemChoiceCompositeKey;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("CO-ProblemChoiceRepository")

public interface ProblemChoiceRepository extends JpaRepository<ProblemChoice, ProblemChoiceCompositeKey>{

		@Query(value="select c from ProblemChoice c where c.probID = :id")
		public List<ProblemChoice>findAllByProbID(@Param("id") Problem id);
}
