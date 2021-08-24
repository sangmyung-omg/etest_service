package com.tmax.eTest.TestStudio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.ProblemChoiceCompositeKey;

public interface ProbChoiceRepositoryTs extends JpaRepository<ProblemChoice, ProblemChoiceCompositeKey> {
	
	List<ProblemChoice> findByProbIDOnlyIs(Integer probID);
}
