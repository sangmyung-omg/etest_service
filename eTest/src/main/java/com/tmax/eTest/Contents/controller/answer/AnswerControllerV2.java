package com.tmax.eTest.Contents.controller.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Contents.dto.answer.SolutionDTO;
import com.tmax.eTest.Contents.dto.problem.AnswerInputDTO;
import com.tmax.eTest.Contents.service.AnswerServicesBase;
import com.tmax.eTest.LRS.dto.GetStatementInfoDTO;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.LRS.util.LRSAPIManager;

/**
 * Improve logics from CBT and Adjust to new API requirements
 * @author Sangmyung Lee
 */

// @PropertySource("classpath:application.properties")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/contents" + "/v2")
public class AnswerControllerV2 {
	
	@Autowired
	@Qualifier("AnswerServicesV1")
	AnswerServicesBase answerServices;

	@Autowired
	LRSAPIManager lrsApiManager;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PostMapping(value="problems/{probId}/answer-check", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> checkAnswer(HttpServletRequest request,
											  @PathVariable("probId") Integer probId,
											  @RequestBody AnswerInputDTO inputDto) throws Exception {
											//   @RequestBody AnswerInputDTO inputDto) throws Exception {
		log.info("> answer-check logic start!");
		Map<String, Object> result = new HashMap<String, Object>();
		String userId = "";
		List<StatementDTO> lrsbody = new ArrayList<StatementDTO>();						
		// input body check
		if (inputDto != null) {
			log.info(inputDto.toString());
			if (inputDto.getLrsbody() != null)
				lrsbody = inputDto.getLrsbody();
			else {
				log.info("lrsbody is null!");
				result.put("resultMessage", "lrsbody is null!");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} else {
			log.info("request body is null!");
			result.put("error", "request body is null");
			return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
		}
		
		// 회원, 비회원 판별
		String header = request.getHeader("Authorization");
		String logText = "";
		if (header == null) {																// 비회원일 때 토큰 null
			if (inputDto.getNrUuid() != null) {
				userId = inputDto.getNrUuid();
				logText += "header.Authorization is null. Unregistered user. / NR-UUID = " + userId;
			}
			else {																			// 비회원용 UUID도 없으면 유저 정보가 없어서 LRS 저장 불가.
				log.info("NR-UUID also null. No user info.");
				result.put("error", "header.Authorization is null. And NR-UUID also null. No user info.");
				return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} else {																			// 회원이면 토큰 파싱하여 유저 아이디 꺼냄
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userId = parseInfo.get("userUuid").toString();
				logText += "Token exists. Registered user. / user UUID = " + userId;
			} catch (Exception e) {															// 토큰 파싱 에러
				log.info("error : cannot parse jwt token, " + e.getMessage());
				result.put("error", e.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		// Inform user identification status
		log.info(logText);
		
		// lrsbody의 유저가 입력한 답 꺼내서 정답과 비교해 정답여부 반환. 1 - 정답 / 0 - 오답 / -1 - 정답 없는 문제
		int isCorrect = answerServices.evaluateIfCorrect(probId, lrsbody);
		for (Integer i=0; i < lrsbody.size(); i++) {
			StatementDTO dto = lrsbody.get(i);
			if (dto.getActionType().equalsIgnoreCase("submit")) {
				if (isCorrect == 0) {
					lrsbody.get(i).setIsCorrect(0);
				} else if (isCorrect == 1) {
					lrsbody.get(i).setIsCorrect(1);
				} else if (isCorrect == -1) {
					lrsbody.get(i).setIsCorrect(null);
				}
			}
			if (dto.getUserId().equalsIgnoreCase("")) {			// 비회원 자가진단이라면, 유저의 아이디가 없이 넘어옴. 근데, 나중 가입 고려해서 임의의 uuid 세팅.
				lrsbody.get(i).setUserId(userId);
			}
		}

		// LRS에 statement 저장
		List<Integer> queryResult = lrsApiManager.saveStatementList(lrsbody);

		// 결과 반환
		if (queryResult.size() == 0) {
			if (isCorrect == -1)
				result.put("resultMessage", "LRS successfully updated & the value '-1' returned for isCorrect since there's no correct answer for tendency problems");
			else
				result.put("resultMessage", "LRS successfully updated & isCorrect successfully returned");
			result.put("isCorrect", isCorrect);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			result.put("resultMessage", "error: some of LRS updates are not proceeded");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value="/problems/{setId}/solution", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> problemTemp1(@PathVariable("setId") String setId) throws Exception{
		log.info("> solution logic start!");
		Map<String, Object> result = new HashMap<String, Object>();
		log.info("Set ID : " + setId);

		// prepare for LRS GETStatement input
		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.setContainExtension(Arrays.asList(setId));

		log.info("Getting LRS statement list......");
		List<StatementDTO> lrsQuery = lrsApiManager.getStatementList(input);

		List<Integer> probIdList = new ArrayList<Integer>();
		Map<Integer, List<String>> probAnswerMap = new HashMap<Integer, List<String>>();
		
		// LRS의 TIMESTAMP를 기준으로 정렬된 statement들 (알아서 order by timestamp asc 으로 오나봄)
		for (StatementDTO dto : lrsQuery) {
			Integer probId = Integer.parseInt(dto.getSourceId());
			// 문제 푼 순서대로 probId 저장. (-> 이게 문제 풀이 페이지에서의 1번, 2번, ..., 20 or 30번 등)
			if (!probIdList.contains(probId)) {
				probIdList.add(probId);

				// user_answer format
				String answer = dto.getUserAnswer();
				if (answer.contains("[") && answer.contains("]")) {
					answer.replace("[", "");
					answer.replace("]", "");
				}
				// mupltiple answer check
				if (answer.contains(",")) {
					probAnswerMap.put(probId, Arrays.asList(dto.getUserAnswer().split(",")));
				} else {
					probAnswerMap.put(probId, Arrays.asList(dto.getUserAnswer()));
				}
			}
		}
		log.info("probIdList : " + probIdList.toString());

		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		try {
			Map<Integer, SolutionDTO> data = answerServices.getParsedMultipleSolutions(probIdList);
			log.info("Solution queryResult length : " + Integer.toString(data.size()));
			for (Integer probId : probIdList) {
				data.get(probId).setUserAnswer(probAnswerMap.get(probId));
				solutions.add(data.get(probId));

				// 컴포넌트 분리
				
			}
		}catch(Exception e) {
			result.put("resultMessage", "error : "+e.getMessage());
		}
		
		result.put("resultMessage", "Successfully returned");
		result.put("solutions", solutions);
		log.info(result.toString());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// 종료 시 LRS Statement 등록
	@PostMapping(value="/quit", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> insertQuitStatement(HttpServletRequest request, @RequestBody AnswerInputDTO inputDto) {
		log.info("> Quit logic start!");
		Map<String, Object> result = new HashMap<String, Object>();
		String userId = "";
		List<StatementDTO> lrsbody = new ArrayList<StatementDTO>();	

		// input body check
		if (inputDto != null) {
			log.info(inputDto.toString());
			if (inputDto.getLrsbody() != null)
				lrsbody = inputDto.getLrsbody();
			else {
				log.info("lrsbody is null!");
				result.put("resultMessage", "lrsbody is null!");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else {
			log.info("request body is null!");
			result.put("error", "request body is null");
			return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
		}

		// 회원, 비회원 판별
		String header = request.getHeader("Authorization");
		String logText = "";
		if (header == null) {																// 비회원일 때 토큰 null
			if (inputDto.getNrUuid() != null) {
				userId = inputDto.getNrUuid();
				logText += "header.Authorization is null. Unregistered user. / NR-UUID = " + userId;
			}
			else {																			// 비회원용 UUID도 없으면 유저 정보가 없어서 LRS 저장 불가.
				log.info("NR-UUID also null. No user info.");
				result.put("error", "header.Authorization is null. And NR-UUID also null. No user info.");
				return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} else {																			// 회원이면 토큰 파싱하여 유저 아이디 꺼냄
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userId = parseInfo.get("userUuid").toString();
				logText += "Token exists. Registered user. / user UUID = " + userId;
			} catch (Exception e) {															// 토큰 파싱 에러
				log.info("error : cannot parse jwt token, " + e.getMessage());
				result.put("error", e.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		log.info(logText);

		for (Integer i=0; i < lrsbody.size(); i++) {
			StatementDTO dto = lrsbody.get(i);
			if (dto.getUserId().equalsIgnoreCase("")) {			// 비회원 자가진단이라면, 유저의 아이디가 없이 넘어옴. 근데, 나중 가입 고려해서 임의의 uuid 세팅.
				lrsbody.get(i).setUserId(userId);
			}
		}

		try {
			// LRS에 statement 저장
			List<Integer> queryResult = lrsApiManager.saveStatementList(lrsbody);
			
			// 결과 반환
			if (queryResult.size() == 0) {
				result.put("resultMessage", "LRS successfully updated");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				log.info("error: some of LRS updates are not successfully proceeded");
				log.debug(queryResult.toString());
				result.put("resultMessage", "error: some of LRS updates are not successfully proceeded");
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.info("error: " + e.getMessage());
			result.put("resultMessage", "error: " + e.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
