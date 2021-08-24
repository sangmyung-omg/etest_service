package com.tmax.eTest.TestStudio.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;

public interface DiagProblemRepositoryTs extends JpaRepository<DiagnosisProblem, Integer> {

	List<DiagnosisProblem> findByCurriculumIdIs(int curriculumID, Sort sort);
}	
