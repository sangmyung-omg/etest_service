package com.tmax.eTest.Contents.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;

@Service
public class AnswerServices {
	
	@Autowired
	ProblemRepository problemRepo;
	
	@Autowired
	ProblemChoiceRepository probChoiceRepo;
	
	
	public Map<String, Object> getProblemSolution(Integer problemID) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<Problem> problemOpt = problemRepo.findById(problemID);
		
		if(problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution",problem.getSolution());
			
		}else {
			throw new NoDataException(problemID);
		}
		return output;
	}
	
	public Map<String, Object> getSolutionMaterial(Integer problemID) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<Problem> problemOpt = problemRepo.findById(problemID);
		
		if(problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution",problem.getSolution());
			output.put("material",problem.getSource());
			
		}else {
			throw new NoDataException(problemID);
		}
		return output;
	}
	

}
