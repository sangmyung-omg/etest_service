package com.tmax.eTest.Contents.controller;

import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tmax.eTest.Contents.answer.service.AnswerServices;
import com.tmax.eTest.Contents.service.problem.ProblemServices;



@RestController
public class AnswerController {
	
	@Autowired
	AnswerServices answerServices;
	
	@Autowired
	ProblemServices problemService;
	
	@GetMapping(value="/{id}/answer-check", produces = "application/json; charset=utf-8")
	public boolean checkAnswer(@PathVariable("id") long id, @RequestParam String answer) throws Exception {
		Map<String, Object> data = null;
		data = answerServices.getProblemSolution(id);
		String inputString = data.get("solution").toString();
		
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject)parser.parse(inputString);
		
		String temp = obj.get("answer").toString().replaceAll("\\[", "");
		temp = temp.replaceAll("\\]","");
		
		if(temp.equals(answer)) {
			
			return true;
		} else {
			return false;
		}
			
		
	}

}
