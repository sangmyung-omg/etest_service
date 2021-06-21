package com.tmax.eTest.Report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelfDiagnosisReportController {
	
	@GetMapping(value="/report/diagnosisResult/{id}", produces = "application/json; charset=utf-8")
	public Map<String, Object> diagnosisResult(@PathVariable("id") long id) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();

		return output;
	}
	
	@GetMapping(value="/report/diagnosisResult/{id}/{part}", produces = "application/json; charset=utf-8")
	public Map<String, Object> partDiagnosisResult(
			@PathVariable("id") long id,
			@PathVariable("part") String part) throws Exception{
		
		Map<String, Object> output = new HashMap<String, Object>();

		return output;
	}
	
	
//	
//	@GetMapping(value="problems/{id}/answer-check", produces = "application/json; charset=utf-8")
//	public boolean checkAnswer(@PathVariable("id") long id, @RequestParam String answer) throws Exception {
//		Map<String, Object> data = null;
//		data = answerServices.getProblemSolution(id);
//		String inputString = data.get("solution").toString();
//		
//		JSONParser parser = new JSONParser();
//		JSONObject obj = (JSONObject)parser.parse(inputString);
//		
//		String temp = obj.get("answer").toString().replaceAll("\\[", "");
//		temp = temp.replaceAll("\\]","");
//		
//		if(temp.equals(answer)) {
//			
//			
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	@GetMapping(value="/problems/{id}/solution", produces = "application/json; charset=utf-8")
//	public Map<String, Object> problem(@PathVariable("id") long id) throws Exception{
//		Map<String, Object> output = new HashMap<String, Object>();
//		Map<String, Object> data = null;
//		try {
//			data = answerServices.getSolutionMaterial(id);
//			output.put("resultMessage", "success");
//			output.put("data", data);
//		}catch(NoDataException e) {
//			output.put("resultMessage", "Failed: "+e.getMessage());
//		}
//		
//		return output;
//	}
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
//		
//		
//		return true;
//	}

}
