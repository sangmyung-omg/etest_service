package com.tmax.eTest.Contents.repository;

import java.util.List;

import com.tmax.eTest.Common.model.problem.Problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CO-ProblemRepository")

public interface ProblemRepository extends JpaRepository<Problem, Integer>{
	
	List<Problem> findByCategoryOrderByDiagnosisInfoCurriculumIdAscDiagnosisInfoOrderNumAscDifficultyAsc(String type);
	
	List<Problem> findByCategory(String type);

	// By S.M.Lee
	List<Problem> findByProbIDIn(List<Integer> probIdList);
}
