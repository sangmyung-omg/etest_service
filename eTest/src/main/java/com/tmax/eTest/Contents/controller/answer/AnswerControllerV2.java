package com.tmax.eTest.Contents.controller.answer;

import java.text.ParseException;
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

import io.jsonwebtoken.JwtException;
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
											  @RequestBody AnswerInputDTO inputDto) {
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
		
		// ??????, ????????? ??????
		String header = request.getHeader("Authorization");
		String logText = "";
		if (header == null) {																// ???????????? ??? ?????? null
			if (inputDto.getNrUuid() != null) {
				userId = inputDto.getNrUuid();
				logText += "header.Authorization is null. Unregistered user. / NR-UUID = " + userId;
			}
			else {																			// ???????????? UUID??? ????????? ?????? ????????? ????????? LRS ?????? ??????.
				log.info("NR-UUID also null. No user info.");
				result.put("error", "header.Authorization is null. And NR-UUID also null. No user info.");
				return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} else {																			// ???????????? ?????? ???????????? ?????? ????????? ??????
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userId = parseInfo.get("userUuid").toString();
				logText += "Token exists. Registered user. / user UUID = " + userId;
			} catch (NullPointerException e) {															
				log.info("error : cannot parse jwt token, NullPointerException occurred");
				result.put("error", "error : cannot parse jwt token, NullPointerException occurred");
				return new ResponseEntity<>(result, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			} catch (JwtException e) {														// ?????? ?????? ??????
				log.info("error : cannot parse jwt token, jwtException occurred");
				result.put("error", "error : cannot parse jwt token, jwtException occurred");
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		// Inform user identification status
		log.info(logText);
		
		// lrsbody??? ????????? ????????? ??? ????????? ????????? ????????? ???????????? ??????. 1 - ?????? / 0 - ?????? / -1 - ?????? ?????? ??????
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
			if (dto.getUserId().equalsIgnoreCase("")) {			// ????????? ?????????????????????, ????????? ???????????? ?????? ?????????. ??????, ?????? ?????? ???????????? ????????? uuid ??????.
				lrsbody.get(i).setUserId(userId);
			}
		}

		// LRS??? statement ??????
		List<Integer> queryResult = new ArrayList<Integer>();
		try {
			queryResult = lrsApiManager.saveStatementList(lrsbody);
		} catch (ParseException e) {
			log.info("error : LRSApiManager ParseException occurred.");
			result.put("error", "LRSApiManager ParseException occurred.");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// ?????? ??????
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
	public ResponseEntity<Object> problemTemp1(@PathVariable("setId") String setId) throws ParseException{
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
		
		// LRS??? TIMESTAMP??? ???????????? ????????? statement??? (????????? order by timestamp asc ?????? ?????????)
		for (StatementDTO dto : lrsQuery) {
			Integer probId = Integer.parseInt(dto.getSourceId());
			// ?????? ??? ???????????? probId ??????. (-> ?????? ?????? ?????? ?????????????????? 1???, 2???, ..., 20 or 30??? ???)
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
		Map<Integer, SolutionDTO> data = answerServices.getParsedMultipleSolutions(probIdList);
		log.info("Solution queryResult length : " + Integer.toString(data.size()));
		for (Integer probId : probIdList) {
			data.get(probId).setUserAnswer(probAnswerMap.get(probId));
			solutions.add(data.get(probId));
		}
		result.put("resultMessage", "Successfully returned");
		result.put("solutions", solutions);
		log.info(result.toString());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// ?????? ??? LRS Statement ??????
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

		// ??????, ????????? ??????
		String header = request.getHeader("Authorization");
		String logText = "";
		if (header == null) {																// ???????????? ??? ?????? null
			if (inputDto.getNrUuid() != null) {
				userId = inputDto.getNrUuid();
				logText += "header.Authorization is null. Unregistered user. / NR-UUID = " + userId;
			}
			else {																			// ???????????? UUID??? ????????? ?????? ????????? ????????? LRS ?????? ??????.
				log.info("NR-UUID also null. No user info.");
				result.put("error", "header.Authorization is null. And NR-UUID also null. No user info.");
				return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
			}
		} else {																			// ???????????? ?????? ???????????? ?????? ????????? ??????
			String token = request.getHeader("Authorization").replace("Bearer ","");
			try {
				Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
				userId = parseInfo.get("userUuid").toString();
				logText += "Token exists. Registered user. / user UUID = " + userId;
			} catch (NullPointerException e) {
				log.info("error : NullPointerException occurred.");
				result.put("error", "NullPointerException occurred.");
				return new ResponseEntity<>(result, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		 	} catch (JwtException e) {															// ?????? ?????? ??????
				log.info("error : cannot parse jwt token, JwtException occurred.");
				result.put("error", "cannot parse jwt token, JwtException occurred.");
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		log.info(logText);

		for (Integer i=0; i < lrsbody.size(); i++) {
			StatementDTO dto = lrsbody.get(i);
			if (dto.getUserId().equalsIgnoreCase("")) {			// ????????? ?????????????????????, ????????? ???????????? ?????? ?????????. ??????, ?????? ?????? ???????????? ????????? uuid ??????.
				lrsbody.get(i).setUserId(userId);
			}
		}

		try {
			// LRS??? statement ??????
			List<Integer> queryResult = lrsApiManager.saveStatementList(lrsbody);
			
			// ?????? ??????
			if (queryResult.size() == 0) {
				result.put("resultMessage", "LRS successfully updated");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				log.info("error: some of LRS updates are not successfully proceeded");
				log.debug(queryResult.toString());
				result.put("resultMessage", "error: some of LRS updates are not successfully proceeded");
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ParseException e) {
			log.info("error: ParseException occurred.");
			result.put("resultMessage", "error: ParseException occurred.");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
