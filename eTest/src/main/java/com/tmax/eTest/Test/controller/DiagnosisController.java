package com.tmax.eTest.Test.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Test.dto.ChapterMasteryDTO;
import com.tmax.eTest.Test.dto.UKMasteryDTO;
import com.tmax.eTest.Test.dto.UserCurrentInfoInput;
import com.tmax.eTest.Test.service.CurriculumService;
import com.tmax.eTest.Test.service.MasteryService;
import com.tmax.eTest.Test.service.ProblemService;
import com.tmax.eTest.Test.service.UserInfoService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DiagnosisController {
	
	@Autowired
	UserInfoService userService;
	
	@Autowired
	ProblemService problemService;
	
	@Autowired
	MasteryService	masteryService;
	
	@Autowired
	CurriculumService curriculumService;
	
	@GetMapping(value = "/diagnosis/tendency", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisTendencyProblems() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultMessage", "Successfully returned");
		List<Integer> list = new ArrayList<Integer>();
		list.add(11);
		list.add(12);
		list.add(13);
		list.add(14);
		list.add(15);
		list.add(16);
		map.put("tendencyProblems", list);
		
		if (map.containsKey("error")) {
			map.put("resultMessage", map.get("error"));
			map.remove("error");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping(value = "/diagnosis/knowledge", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisKnowledgeProblems() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultMessage", "Successfully returned");
		List<List<Integer>> list = new ArrayList<List<Integer>>();
		for (int i=0; i<9; i++) {
			List<Integer> list2 = new ArrayList<Integer>();
			for (int j=3*i+1; j<3*(i+1)+1; j++) {
				list2.add(j);
			}
			list.add(list2);
		}
		map.put("knowledgeProblems", list);
		
		if (map.containsKey("error")) {
			map.put("resultMessage", map.get("error"));
			map.remove("error");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@GetMapping(value = "/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getMinitestProblems() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultMessage", "Successfully returned");
		List<Integer> list = new ArrayList<>();
		for (int i=0; i<20; i++) {
			list.add(i+1);
		}
		map.put("minitestProblems", list);
		
		if (map.containsKey("error")) {
			map.put("resultMessage", map.get("error"));
			map.remove("error");
			return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
