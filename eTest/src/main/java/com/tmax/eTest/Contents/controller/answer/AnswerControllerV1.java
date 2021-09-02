package com.tmax.eTest.Contents.controller.answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.tmax.eTest.Contents.dto.CustomizedSolutionDTO;
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
	
	@PostMapping(value="problems/{probId}/answer-check", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> checkAnswer(@AuthenticationPrincipal PrincipalDetails principalDetails,
											  @PathVariable("probId") Integer probId,
											  @RequestBody ArrayList<StatementDTO> lrsbody) throws Exception {
		logger.info("> answer-check logic start!");
		Map<String, Object> res = new HashMap<String, Object>();
		String userId = "";
		
		try {
			userId = principalDetails.getUserUuid();	
		} catch (Exception e) {
			logger.info("userUuid : " + e.getMessage());
		}
		// lrsbody의 유저가 입력한 답 꺼내서 정답과 비교해 정답여부 반환. 1 - 정답 / 0 - 오답 / -1 - 정답 없는 문제
		int isCorrect = answerServices.evaluateIfCorrect(probId, lrsbody);
		for (Integer i=0; i < lrsbody.size(); i ++) {
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
			if (isCorrect == -1) res.put("resultMessage", "LRS successfully updated & the value '-1' returned for isCorrect since there's no correct answer for tendency problems");
			else res.put("resultMessage", "LRS successfully updated & isCorrect successfully returned");
			res.put("isCorrect", isCorrect);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			res.put("resultMessage", "error: some of LRS updates are not proceeded");
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
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
