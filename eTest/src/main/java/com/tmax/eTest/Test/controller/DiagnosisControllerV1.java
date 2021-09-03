package com.tmax.eTest.Test.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.LRS.model.Statement;
import com.tmax.eTest.LRS.repository.StatementRepository;
import com.tmax.eTest.LRS.util.LRSAPIManager;
import com.tmax.eTest.Test.config.TestPathConstant;
// import com.tmax.eTest.Test.dto.DiagnosisOutputDTO;
import com.tmax.eTest.Test.service.ProblemServiceBase;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = TestPathConstant.apiPrefix + "/v1")
public class DiagnosisControllerV1 {
	
	@Autowired
	@Qualifier("ProblemServiceV1")
	ProblemServiceBase problemService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	LRSAPIManager lrsApiManager;

	@Autowired
	StatementRepository statementRepository;
	
	@GetMapping(value = "/diagnosis", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getDiagnosisProblems(HttpServletRequest request) throws Exception {
		log.info("> diagnosis logic start!");

		Map<String, Object> res = new HashMap<String, Object>();
		
		// 성향 문제 조회
		try{
			// Map<String, Object> res = new HashMap<String, Object>();
			res = problemService.getDiagnosisTendencyProblems();
			
		} catch (Exception E){
			log.info(E.getMessage());
			res.put("resultMessage", "Internal Server Error.\n" + E.getMessage());
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 지식 문제 조회
		Integer firstProbId = 0;		// Statement 저장용 첫 문제 ID 저장.
		try{
			List<List<Integer>> problems = (List<List<Integer>>) problemService.getDiagnosisKnowledgeProblems().get("knowledgeProblems");
			firstProbId = problems.get(0).get(0);
			res.put("knowledgeProblems", problems);
			
		} catch (Exception E){
			res.clear();
			log.info(E.getMessage());
			res.put("resultMessage", "Internal Server Error.\n" + E.getMessage());
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String userUuid = "";

		// 회원 / 비회원 판별
		String header = request.getHeader("Authorization");
		if (header == null) {
			log.info("header is null : Unregistered User. So, random NR-UUID is given.");
			userUuid = "NR-" + UUID.randomUUID().toString();
			res.put("NRUuid", userUuid);
		} else {
			String token = header.replace("Bearer ","");
			Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);

			userUuid = parseInfo.get("userUuid").toString();
			res.put("NRUuid", "");

			log.info("User ID : " + userUuid);
		}

		// Random 세트 아이디 발급
		String diagProbSetId = UUID.randomUUID().toString();

		// 현재시간
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.KOREA);
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		// Setting statement format
		Statement statement = new Statement();
		statement.setStatementId(UUID.randomUUID().toString());
		statement.setUserId(userUuid);
		statement.setActionType("enter");
		statement.setSourceType("diagnosis_pattern");
		statement.setTimestamp(dateFormat.format(new Date()));		// LRS는 ISO 8601 포맷에서 끝의 Z 빼기로.
		statement.setPlatform("Kofia");
		statement.setSourceId(Integer.toString(firstProbId));
		statement.setExtension("{\"diagProbSetId\":\""+ diagProbSetId + "\",\"guessAlarm\":0}");

		log.info("Save statement : " + statement.toString());
		try {
			statementRepository.save(statement);
		} catch (Exception e) {
			log.info("error : insert failed - " + e.getMessage());
			res.put("resultMessage", "error : statement insert fail - " + e.getMessage());
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		res.put("resultMessage", "Successfully returned.");
		res.put("diagProbSetId", diagProbSetId);
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
	
	// 인가 필요 페이지
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/minitest", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> getMinitestProblems(@AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
		log.info("> minitest logic start!");
		Map<String, Object> result = new HashMap<String, Object>();

		// Check User UUID from request header
		String userUuid = principalDetails.getUserUuid();
		// String token = request.getHeader("Authorization").replace("Bearer ","");
		// Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);

		// String userUuid = parseInfo.get("userUuid").toString();

		
		result = problemService.getMinitestProblems(userUuid);
		if (result.containsKey("error")) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			result.put("resultMessage", "Successfully returned");
			return new ResponseEntity<>(result, HttpStatus.OK);
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
