package com.tmax.eTest.Contents.controller.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Auth.dto.PrincipalDetails;
import com.tmax.eTest.Auth.jwt.JwtTokenUtil;
import com.tmax.eTest.Contents.dto.CustomizedSolutionDTO;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path = "/contents" + "/v1")
public class AnswerControllerV1 {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
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
											  @RequestBody ArrayList<StatementDTO> lrsbody) throws Exception {
											//   @RequestBody AnswerInputDTO inputDto) throws Exception {
		logger.info("> answer-check logic start!");
		Map<String, Object> result = new HashMap<String, Object>();
		String userId = "";
		
		// 회원, 비회원 판별
		String header = request.getHeader("Authorization");
		if (header == null) {																// 비회원일 때 토큰 null
			logger.info("header.Authorization is null. No token given.");
			result.put("resultMessage", "header.Authorization is null. No token given.");
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		} else {																			// 회원이면 토큰 파싱하여 유저 아이디 꺼냄
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userId = parseInfo.get("userUuid").toString();
				logger.info("User UUID from header token : " + userId);
			} catch (Exception e) {
				logger.info("error : cannot parse jwt token, " + e.getMessage());
				result.put("error", e.getMessage());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
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
	public Map<String, Object> problem(@PathVariable("setId") String setId) throws Exception{
		logger.info("> solution logic start!");
		Map<String, Object> output = new HashMap<String, Object>();
		logger.info("Set ID : " + setId);

		// prepare for LRS GETStatement input
		GetStatementInfoDTO input = new GetStatementInfoDTO();
		input.setContainExtension(Arrays.asList(setId));

		logger.info("Getting LRS statement list......");
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
		logger.info("probIdList : " + probIdList.toString());

		List<CustomizedSolutionDTO> solutions = new ArrayList<CustomizedSolutionDTO>();
		try {
			Map<Integer, CustomizedSolutionDTO> data = answerServices.getMultipleSolutions(probIdList);
			logger.info("Solution queryResult length : " + Integer.toString(data.size()));
			for (Integer probId : probIdList) {
				data.get(probId).setUserAnswer(probAnswerMap.get(probId));
				solutions.add(data.get(probId));
			}
		}catch(Exception e) {
			output.put("resultMessage", "error : "+e.getMessage());
		}
		
		output.put("resultMessage", "Successfully returned");
		output.put("solutions", solutions);
		return output;
	}

}
