package com.tmax.eTest.Common.repository.problem;

import com.tmax.eTest.Common.model.problem.TestProblem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestProblemRepo extends JpaRepository<TestProblem, Integer>{
    
}
