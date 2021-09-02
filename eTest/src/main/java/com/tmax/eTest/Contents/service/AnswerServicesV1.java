package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.dto.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.StatementDTO;

/**
 * Improve logics from CBT and Adjust to new API requirements
 * 
 * @author Sangmyung Lee
 */

@Service("AnswerServicesV1")
public class AnswerServicesV1 implements AnswerServicesBase {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	ProblemRepository problemRepo;

	@Autowired
	ProblemChoiceRepository probChoiceRepo;
	
	public Integer evaluateIfCorrect(Integer probId, ArrayList<StatementDTO> lrsbody) throws Exception {
		String userAnswer = lrsbody.get(0).getUserAnswer();
		logger.info("Problem ID : " + Integer.toString(probId) + ", user answer : " + userAnswer);
		logger.info("Inserted LRS body : " + lrsbody.toString());

		Map<String, Object> data = new HashMap<String, Object>();

		try {
			data = getProblemSolution(probId);
		} catch (Exception e) {
			logger.info(e.toString());
			return 0;
		}
		String inputString = data.get("solution").toString();
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(inputString);
		logger.info("json : " + jsonArray.toJSONString() + ", " + Integer.toString(probId));

		String correctAnswer = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			logger.info("Item #" + Integer.toString(i) + " of jsonArray : " + jsonArray.get(i).toString());
			JSONObject json = (JSONObject) jsonArray.get(i);
			String type = json.get("type").toString();
			if (type.contains("CORRECT_ANSWER")) {
				correctAnswer = json.get("data").toString();
				if (correctAnswer.equals("[]")) {
					logger.info("A tendency problem has no answer.");
					return -1;
				}
				if (correctAnswer.contains("[")){
					correctAnswer = correctAnswer.replaceAll("\\[", "");
				}
				if (correctAnswer.contains("]")) {
					correctAnswer = correctAnswer.replaceAll("\\]", "");
				}
				logger.info("correctAnswer : " + correctAnswer);
			}
			logger.info("type : " + type);
		}
		logger.info("Total solution string : " + inputString);
		logger.info("Correct Answer is... " + correctAnswer);
		if (correctAnswer.equalsIgnoreCase(userAnswer)) return 1;
		else return 0;
	}

	public Map<String, Object> getProblemSolution(Integer problemID) throws Exception {
		Map<String, Object> output = new HashMap<String, Object>();

		logger.info("Getting solution info......");
		Optional<Problem> problemOpt = problemRepo.findById(problemID);

		if (problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution", problem.getSolution());

		} else {
			throw new NoDataException(problemID);
		}
		return output;
	}

	public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList) {
		logger.info("Getting multiple solution infos......");
		List<Problem> probList = problemRepo.findByProbIDIn(probIdList);
		logger.info(probList.get(0).getQuestion());
		logger.info(probList.get(0).getSolution());
		Map<Integer, CustomizedSolutionDTO> solutionMap = new HashMap<Integer, CustomizedSolutionDTO>();
		for (Problem dto : probList) {
			CustomizedSolutionDTO solutionInfo = new CustomizedSolutionDTO();
			solutionInfo.setProbId(dto.getProbID());
			solutionInfo.setMaterial(dto.getSource());
			solutionInfo.setSolution(dto.getSolution());
			// logger.info(dto.getSolution());
			solutionMap.put(dto.getProbID(), solutionInfo);
		}
		return solutionMap;
	}

	public Map<String, Object> getSolutionMaterial(Integer problemID) throws Exception {
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<Problem> problemOpt = problemRepo.findById(problemID);

		if (problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution", problem.getSolution());
			output.put("material", problem.getSource());

		} else {
			throw new NoDataException(problemID);
		}
		return output;
	}

}
