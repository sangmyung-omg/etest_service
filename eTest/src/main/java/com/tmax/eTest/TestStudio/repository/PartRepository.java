package com.tmax.eTest.TestStudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.Part;

public interface PartRepository extends JpaRepository<Part, Integer> {
	
}