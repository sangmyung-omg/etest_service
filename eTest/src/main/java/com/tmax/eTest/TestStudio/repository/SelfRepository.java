package com.tmax.eTest.TestStudio.repository;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfRepository extends JpaRepository<DiagnosisProblem, Integer>{
    
	@EntityGraph(attributePaths = {"problem","problem.testInfo"}, type = EntityGraph.EntityGraphType.FETCH)
	List<DiagnosisProblem> findByCurriculum_CurriculumId(Integer curriculumId);
	
}