package com.tmax.eTest.Common.repository.problem;

import com.tmax.eTest.Common.model.problem.Problem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepo extends JpaRepository<Problem, Integer>{
    
}
