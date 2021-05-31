package com.tmax.eTest.Contents.controller; 

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.service.problem.ProblemServices;


@RestController
public class ProblemController {
	
	@Autowired
	ProblemServices problemService;
	
	
	@GetMapping(value="/problems/{id}", produces = "application/json; charset=utf-8")
	public Map<String, Object> problem(@PathVariable("id") long id) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Map<String, Object> data = null;
		try {
			data = problemService.getProblem(id);
			output.put("resultMessage", "success");
			output.put("data", data);
		}catch(NoDataException e) {
			output.put("resultMessage", "Failed: "+e.getMessage());
		}
		
		return output;
	}
}
