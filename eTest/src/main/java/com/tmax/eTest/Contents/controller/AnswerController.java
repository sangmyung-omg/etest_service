package com.tmax.eTest.Contents.controller;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.tmax.eTest.Contents.exception.problem.NoDataException;

import com.tmax.eTest.Contents.service.AnswerServices;
import com.tmax.eTest.Contents.service.ProblemServices;




@RestController
public class AnswerController {
	
	@Autowired
	AnswerServices answerServices;
	
	@Autowired
	ProblemServices problemService;
	
	@GetMapping(value="problems/{id}/answer-check", produces = "application/json; charset=utf-8")
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
	
	@GetMapping(value="/problems/{id}/solution", produces = "application/json; charset=utf-8")
	public Map<String, Object> problem(@PathVariable("id") long id) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Map<String, Object> data = null;
		try {
			data = answerServices.getSolutionMaterial(id);
			output.put("resultMessage", "success");
			output.put("data", data);
		}catch(NoDataException e) {
			output.put("resultMessage", "Failed: "+e.getMessage());
		}
		
		return output;
	}
//	
//	@GetMapping(value="/test", produces = "application/json; charset=utf-8")
//	public boolean test(
//			@RequestParam String actionType, 
//			@RequestParam String isCorrect, 
//			@RequestParam String sourceType, 
//			@RequestParam String timestamp,  
//			@RequestParam String userId
//			) 
//	{
//		final String url = "http://192.168.153.132:8080/SaveStatement";
//		
//		RestTemplate restTemplate = new RestTemplate();
//		
//		
////		LRS lrs= new LRS(actionType,isCorrect,sourceType,timestamp,userId);
//		
////		Map<String, String> map = new HashMap<>();
////		
////		map.put("actionType", actionType);
////		map.put("isCorrect", isCorrect);
////		map.put("sourceType", sourceType);
////		map.put("timestamp", timestamp);
////		map.put("userId", userId);
////		
//		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		
//		params.add("actionType", actionType);
//		params.add("isCorrect", isCorrect);
//		params.add("sourceType", sourceType);
//		params.add("timestamp", timestamp);
//		params.add("userId", userId);
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		
////		HttpEntity<Map<String,String>> entity = new HttpEntity<Map<String,String>>(map,headers);
//		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);
//		
////		ResponseEntity<String> response = restTemplate.exchange(url,
////				HttpMethod.POST,
////				entity,
////				String.class);
//		
////		System.out.println(response.getBody());
//		
//		
//		
////		
////		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST,params,String.class);
//		
//		ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
//
//		System.out.println("Result = " + result.getBody());
//		
//		
//		
//		return true;
//	}

}
