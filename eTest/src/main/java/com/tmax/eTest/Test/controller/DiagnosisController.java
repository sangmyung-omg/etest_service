package com.tmax.eTest.Test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Test.service.ProblemService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DiagnosisController {
	
	@Autowired
	ProblemService problemService;
	
	@GetMapping(value = "/diagnosis/tendency", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisTendencyProblems() throws Exception {

		try{
			Map<String, Object> res = problemService.getDiagnosisTendencyProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (Exception E){
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/diagnosis/knowledge", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisKnowledgeProblems() throws Exception {
		try{
			Map<String, Object> res = problemService.getDiagnosisKnowledgeProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (Exception E){
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("resultMessage", "Internal Server Error.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getMinitestProblems(@RequestParam(required=false) Integer set_num) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		// dummy return
//		List<Integer> list = new ArrayList<>();
//		for (int i=0; i<7; i++) {
//			list.add(i+1);
//		}
//		map.put("minitestProblems", list);
		
		// search minitest with setnum
		Map<String, Object> re = problemService.getMinitestProblems(set_num);
		if (re.containsKey("error")) {
			map.put("resultMessage", re.get("error"));
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			map.put("minitestProblems", re.get("minitestProblems"));
		}
		
		map.put("resultMessage", "Successfully returned");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
