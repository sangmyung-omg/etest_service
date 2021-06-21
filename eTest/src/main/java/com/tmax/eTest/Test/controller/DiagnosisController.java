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
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	
	
	
	
	@GetMapping(value = "/ChapterNameList", produces = "application/json; charset=utf-8")
	public Map<String, Object> getChapterNameList(@RequestParam String grade, @RequestParam String semester) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (grade != null && semester != null) {
			System.out.println(grade + "-" + semester);
			map.put("resultMessage", "Successfully returned");
			map.put("chapterList", curriculumService.getChapterNameList(grade, semester));
		} else {
			map.put("resultMessage", "error : At least one of the input variables is empty");
		}
		return map;			
	}
	
	@PostMapping(value = "/UserCurrentInfo", produces = "application/json; charset=utf-8")
	public Map<String, String> updateUserCurrentInfo(@RequestBody UserCurrentInfoInput param) throws Exception {
		String userId;
		if (param.getUserId() == null) {
			userId = UUID.randomUUID().toString();
			System.out.println("NO UserId");
		} else userId = param.getUserId();
		String grade = param.getGrade();
		String semester = param.getSemester();
		String chapter = param.getChapter();
		System.out.println("DiagnosisController UserCurrentInfo chapter : " + chapter);
		
		System.out.println(userId + " : " + grade + "-" + semester + " " + chapter);
		
		Map<String,String> map = new HashMap<String,String>();
		if (chapter != null) {
			map.put("resultMessage", userService.updateUserCurrentInfo(userId, grade, semester, chapter));			
		} else {
			map.put("resultMessage", "error : chapter is null");
		}
		return map;
	}
	
	@GetMapping(value = "/NextProblemSet", produces = "application/json; charset=utf-8")
	public Map<String, Object> getNextProblemSet(
			@RequestParam String userId,
			@RequestParam String diagType,
			@RequestParam String part,
			@RequestParam String is_adaptive) throws Exception {
		if (userId == null) {
			userId = UUID.randomUUID().toString();
			System.out.println("NO UserId");
		}
		System.out.println(userId + ", " + diagType + ", " + part + ", " + is_adaptive);
		Map<String, Object> map = new HashMap<String, Object>();
		if (!(part.equalsIgnoreCase("0") || part.equalsIgnoreCase("1") || part.equalsIgnoreCase("2") || part.equalsIgnoreCase("3") || part.equalsIgnoreCase("4"))) {
			map.put("resultMessage", "Invalid input value : part -> " + part);
			return map;
		}
		List<Map<String, String>> list = problemService.getNextProblemSet(userId, diagType, part, is_adaptive);
		if (list == null || list.size() == 0) {
			map.put("resultMessage", "error : result of getnextProblemSet is null, " + list);
			System.out.println();
		} else {
			if (list.get(0).containsKey("error")) {
				map.put("resultMessage", list.get(0).get("error"));
				return map;
			}
			map.put("resultMessage", "Successfully returned");
			map.put("problemSet", list);			
		}
		return map;
	}
	
	@GetMapping(value="/ChapterMastery", produces = "application/json; charset=utf-8")
	public Map<String, Object> getChapterMastery(
			@RequestParam String userId,
			@RequestParam List<String> ukIdList){
		System.out.println(ukIdList);//607,608,4673,1011,10168,1000,10183,10190,2666,10169
		Map<String, Object> map = new HashMap<String, Object>();
		if (userId != null && ukIdList != null) {
				// 각 UK들이 어떤 파트의, 어떤 대단원의 UK인지 분류 & 대단원 별로 평균
				map = curriculumService.getChapterMastery(userId, ukIdList);

		} else map.put("resultMessage", "No input value : userId");
		
		return map;
	}
}
