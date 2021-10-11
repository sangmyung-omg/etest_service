package com.tmax.eTest.Test.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Test.config.TestPathConstant;
import com.tmax.eTest.Test.service.ProblemServiceBase;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = TestPathConstant.apiPrefix + "/v0")
public class DiagnosisControllerV0 {
	
	@Autowired
    @Qualifier("ProblemServiceV0")
	ProblemServiceBase problemService;
	
	@GetMapping(value = "/diagnosis/tendency", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisTendencyProblems() throws Exception {

		try{
			Map<String, Object> res = problemService.getDiagnosisTendencyProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (NullPointerException E){
			Map<String, Object> ret = new HashMap<String, Object>();
			log.info("Internal Server Error. NullPointerException occurred.");
			ret.put("resultMessage", "Internal Server Error. NullPointerException occurred.");
			return new ResponseEntity<>(ret, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/diagnosis/knowledge", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisKnowledgeProblems() throws Exception {
		try{
			Map<String, Object> res = problemService.getDiagnosisKnowledgeProblems();
			res.put("resultMessage", "Successfully returned.");
			return new ResponseEntity<>(res, HttpStatus.OK);

		} catch (IndexOutOfBoundsException E){
			Map<String, Object> ret = new HashMap<String, Object>();
			log.info("Internal Server Error. IndexOutOfBoundsException occurred.");
			ret.put("resultMessage", "Internal Server Error. IndexOutOfBoundsException occurred.");
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
		Map<String, Object> re = problemService.getMinitestProblemsV0(set_num);
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
