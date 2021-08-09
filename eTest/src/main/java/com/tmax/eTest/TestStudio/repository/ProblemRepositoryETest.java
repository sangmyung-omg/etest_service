package com.tmax.eTest.TestStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.Problem;

public interface ProblemRepositoryETest extends JpaRepository<Problem, Integer> {

}
