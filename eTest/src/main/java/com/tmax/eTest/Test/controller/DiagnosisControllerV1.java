package com.tmax.eTest.Test.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Test.config.TestPathConstant;
import com.tmax.eTest.Test.service.ProblemServiceBase;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = TestPathConstant.apiPrefix + "/v1")
public class DiagnosisControllerV1 {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	@Autowired
	@Qualifier("ProblemServiceV1")
	ProblemServiceBase problemService;
	
	@GetMapping(value = "/diagnosis", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisProblems(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		logger.info("> diagnosis logic start!");
		// logger.info(principalDetails);
		String userUuid = "";
		try {
			userUuid = principalDetails.getUserUuid();
			logger.info("userUuid : " + userUuid);
			
		} catch (Exception e) {
			userUuid = "NR-" + UUID.randomUUID().toString();
			logger.info("userUuid is " + e.getMessage() + " : Unregistered user / So random unregistered UUID is generated : " + userUuid);
		}
		// logger.info(userUuid.toString());
		Map<String, Object> res = new HashMap<String, Object>();

		// 성향 문제 조회
		try{
			res = problemService.getDiagnosisTendencyProblems();
			
		} catch (Exception E){
			logger.info(E.getMessage());
			res.put("resultMessage", "Internal Server Error.\n" + E.getMessage());
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 지식 문제 조회
		try{
			res.put("knowledgeProblems", problemService.getDiagnosisKnowledgeProblems().get("knowledgeProblems"));

		} catch (Exception E){
			res.clear();
			logger.info(E.getMessage());
			res.put("resultMessage", "Internal Server Error.\n" + E.getMessage());
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		res.put("resultMessage", "Successfully returned.");
		res.put("diagProbSetId", UUID.randomUUID().toString());
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	/* 8/13 auth 쪽 백에서 접속한 시간 정보 포함하는 jwt 토큰 미리 발행. (세션 개념)
	*  그 토큰으로 홈페이지 쪽에서 etest 쪽으로 토큰(현재 로그인 중인 사람 정보)과 함께 진입.
	*  etest 프론트에서 해당 토큰과 함께 백으로 자가진단 요청하면, 이쪽 백에서 Authentication 동작 : jwt를 decode해서 그 안의 email 정보로 식별해 USER_MASTER 로부터 유저 정보들 다 조회해옴.
	*  이미 instance에 가지고 있는 유저 정보는 원래 정보 그대로 PrincipalDetails 라는 DTO(?)에 넣어서 들고 있음.
	*  우리는 그 PrincipalDetails에 담겨있는 유저 아이디만 꺼내서 사용하면 됨.
	*
	*  결론 : 프론트에서는 따로 유저 아이디를 위해 jwt 만들 필요 없이 홈페이지에서 발급된 jwt 토큰만 넘겨주면 됨. 백에서는 아래와 같이 받아서 유저 아이디 꺼내서 사용하면 됨.
	*  검증은 추후 프론트 붙이면서 진행
	*/
	@GetMapping(value = "/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getMinitestProblems(@AuthenticationPrincipal PrincipalDetails principalDetails,			
													 @RequestParam String userId) throws Exception {
		logger.info("> minitest logic start!");

		// // search minitest with setnum
		Map<String, Object> re = problemService.getMinitestProblems(userId);
		if (re.containsKey("error")) {
			return new ResponseEntity<>(re, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			re.put("resultMessage", "Successfully returned");
			return new ResponseEntity<>(re, HttpStatus.OK);
		}
		

		// map.put("resultMessage", "Successfully returned");
		// map.put("newProbSetId", "2fc89ea8-fb4b-11eb-9a03-0242ac130003");
		// map.put("minitestProblems", Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));
		// map.put("contiueProbSetId", "429589d8-fbe1-11eb-9a03-0242ac130003");
		// map.put("continueProblems", Arrays.asList(88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107));
		// map.put("continueAnswers", Arrays.asList(1, 4, 3, 5, 2));
		// map.put("isCorrect", Arrays.asList(0, 1, 0, 0, 1));
		// map.put("guessAlarm", 1);
		// return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
