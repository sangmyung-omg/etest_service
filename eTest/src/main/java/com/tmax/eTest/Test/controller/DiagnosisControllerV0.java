package com.tmax.eTest.Test.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Test.config.TestPathConstant;
import com.tmax.eTest.Test.service.ProblemServiceBase;
import com.tmax.eTest.Test.service.ProblemServiceV1;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = TestPathConstant.apiPrefix + "/v0")
public class DiagnosisControllerV0 {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
    @Qualifier("ProblemServiceV0")
	ProblemServiceBase problemService;
	
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
			logger.info(E.getMessage());
			E.printStackTrace();
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
