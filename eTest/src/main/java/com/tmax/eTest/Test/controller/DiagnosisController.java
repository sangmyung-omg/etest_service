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
		Map<String, Object> map = new HashMap<String, Object>();
//		List<Integer> list = new ArrayList<Integer>();
//		list.add(11);
//		list.add(12);
//		list.add(13);
//		list.add(14);
//		list.add(15);
//		list.add(16);
//		map.put("tendencyProblems", list);
		
		// search tendency problems in DB
		Map<String, Object> re = problemService.getDiagnosisProblems("tendency");
		
		if (re.containsKey("error")) {
		map.put("resultMessage", re.get("error"));
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			map.put("tendencyProblems", re.get("tendencyProblems"));
		}
		
		map.put("resultMessage", "Successfully returned");
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping(value = "/diagnosis/knowledge", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisKnowledgeProblems() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
//		List<List<Integer>> list = new ArrayList<List<Integer>>();
//		for (int i=0; i<9; i++) {
//			List<Integer> list2 = new ArrayList<Integer>();
//			for (int j=3*i+1; j<3*(i+1)+1; j++) {
//				list2.add(j);
//			}
//			list.add(list2);
//		}
//		map.put("knowledgeProblems", list);
		
		// search knowledge problems in DB
		Map<String, Object> re = problemService.getDiagnosisProblems("knowledge");
		
		if (re.containsKey("error")) {
		map.put("resultMessage", re.get("error"));
		return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			map.put("knowledgeProblems", re.get("knowledgeProblems"));
		}
		
		map.put("resultMessage", "Successfully returned");
		return new ResponseEntity<>(map, HttpStatus.OK);
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
