package com.tmax.eTest.Common.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmax.eTest.Common.model.problem.Part;

public interface PartRepo extends JpaRepository<Part, Integer>{
    
}
