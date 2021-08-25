package com.tmax.eTest.Contents.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Contents.dto.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.service.AnswerServicesBase;
import com.tmax.eTest.Contents.service.AnswerServicesV1;
import com.tmax.eTest.Contents.service.ProblemServices;
import com.tmax.eTest.Report.dto.lrs.GetStatementInfoDTO;
import com.tmax.eTest.Report.dto.lrs.StatementDTO;
import com.tmax.eTest.Report.util.LRSAPIManager;

import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.EntityResponse;

import io.netty.handler.codec.http.HttpResponseStatus;

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
	ProblemServices problemService;
	
	@Value("${etest.recommend.lrs.host}")
	private String LRS_HOST;
	
	@Value("${etest.recommend.lrs.port}")
	private String LRS_PORT;

	final String LRSServerURI = "http://192.168.153.132:8080";
	// private final String LRSServerURI = "http://" + LRS_HOST + ":" + LRS_PORT;

	@Autowired
	LRSAPIManager lrsApiManager;
	
	@PostMapping(value="problems/{probId}/answer-check", produces = "application/json; charset=utf-8")
	public ResponseEntity<Object> checkAnswer(@PathVariable("probId") Integer probId, @RequestBody ArrayList<StatementDTO> lrsbody) throws Exception {
		
		Map<String, Object> res = new HashMap<String, Object>();
		
		int isCorrect = answerServices.evaluateIfCorrect(probId, lrsbody);
		if (isCorrect == 1) {
			lrsbody.get(0).setIsCorrect(1);
		} else {
			lrsbody.get(0).setIsCorrect(0);
		}

		List<Integer> queryResult = lrsApiManager.saveStatementList(lrsbody);
		logger.info(queryResult.toString());

		if (queryResult.size() == 0) {
			res.put("resultMessage", "LRS successfully updated & isCorrect successfully returned");
			res.put("isCorrect", isCorrect);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			res.put("resultMessage", "error: some of LRS updates are not proceeded");
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value="/problems/{setId}/solution", produces = "application/json; charset=utf-8")
	public Map<String, Object> problem(@PathVariable("setId") String setId) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		logger.info(setId);

		// prepare for LRS GET input
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
