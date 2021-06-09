package com.tmax.eTest.Contents.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tmax.eTest.Contents.model.ProblemChoiceCompositeKey;
import com.tmax.eTest.Contents.model.ProblemChoice;
import com.tmax.eTest.Contents.model.Problem;

public interface ProblemChoiceRepository extends JpaRepository<ProblemChoice, ProblemChoiceCompositeKey>{

		@Query(value="select c from ProblemChoice c where c.probID = :id")
		public List<ProblemChoice>findAllByProbID(@Param("id") Problem id);
}
