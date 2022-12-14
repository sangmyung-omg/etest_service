package com.tmax.eTest.Contents.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.dto.answer.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.dto.answer.SolutionDTO;
import com.tmax.eTest.Contents.dto.problem.ComponentDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.StatementDTO;
import com.tmax.eTest.TestStudio.controller.component.ImageFileServerApiComponentTs;

/**
 * Improve logics from CBT and Adjust to new API requirements
 * 
 * @author Sangmyung Lee
 */

@Slf4j
@Service("AnswerServicesV1")
public class AnswerServicesV1 implements AnswerServicesBase {

	@Autowired
	ProblemRepository problemRepo;

	@Autowired
	ProblemChoiceRepository probChoiceRepo;

	@Autowired
	ImageFileServerApiComponentTs imgFileApi;
	
	public Integer evaluateIfCorrect(Integer probId, List<StatementDTO> lrsbody) {
		String userAnswer = lrsbody.get(0).getUserAnswer();
		log.info("Problem ID : " + Integer.toString(probId) + ", user answer : " + userAnswer);
		// log.info("Inserted LRS body : " + lrsbody.toString());

		Map<String, Object> data = new HashMap<String, Object>();

		data = getProblemSolution(probId);

		String inputString = data.get("solution").toString();
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = new JSONArray();
		try {
			jsonArray = (JSONArray) parser.parse(inputString);
		} catch (ParseException e) {
			log.info("error : json ParsingException occurred.");
		}
		// log.info("json : " + jsonArray.toJSONString() + ", " + Integer.toString(probId));

		String correctAnswer = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			log.info("Item #" + Integer.toString(i) + " of jsonArray : " + jsonArray.get(i).toString());
			JSONObject json = (JSONObject) jsonArray.get(i);
			String type = json.get("type").toString();
			if (type.contains("CORRECT_ANSWER")) {
				correctAnswer = json.get("data").toString();
				if (correctAnswer.equals("[]")) {
					log.info("A tendency problem has no answer.");
					return -1;
				}
				if (correctAnswer.contains("[")){
					correctAnswer = correctAnswer.replaceAll("\\[", "");
				}
				if (correctAnswer.contains("]")) {
					correctAnswer = correctAnswer.replaceAll("\\]", "");
				}
			}
		}
		log.info("Correct Answer is... " + correctAnswer);
		if (correctAnswer.equalsIgnoreCase(userAnswer)) return 1;
		else return 0;
	}

	public Map<String, Object> getProblemSolution(Integer problemID) {
		Map<String, Object> output = new HashMap<String, Object>();

		log.info("Getting solution info......");
		Optional<Problem> problemOpt = problemRepo.findById(problemID);

		if (problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution", problem.getSolution());

		} else {
			log.info("error: Problem No Data Exception");
		}
		return output;
	}

	public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList) {
		log.info("Getting multiple solution infos......");
		List<Problem> probList = problemRepo.findByProbIDIn(probIdList);
		Map<Integer, CustomizedSolutionDTO> solutionMap = new HashMap<Integer, CustomizedSolutionDTO>();
		for (Problem dto : probList) {
			CustomizedSolutionDTO solutionInfo = new CustomizedSolutionDTO();
			solutionInfo.setProbId(dto.getProbID());
			solutionInfo.setMaterial(dto.getSource());
			solutionInfo.setSolution(dto.getSolution());
			solutionMap.put(dto.getProbID(), solutionInfo);
		}
		return solutionMap;
	}

	public Map<Integer, SolutionDTO> getParsedMultipleSolutions(List<Integer> probIdList) {
		log.info("Getting multiple solution infos......");
		List<Problem> probList = problemRepo.findByProbIDIn(probIdList);
		Map<Integer, SolutionDTO> solutionMap = new HashMap<Integer, SolutionDTO>();
		for (Problem dto : probList) {
			SolutionDTO solutionInfo = new SolutionDTO();
			int probId = dto.getProbID();
			solutionInfo.setProbId(probId);
			solutionInfo.setMaterial(dto.getSource());
			// solutionInfo.setSolution(dto.getSolution());

			// Prasing the solution column
			try {
				JSONParser parser = new JSONParser();
				JSONArray solutionJsonArray = (JSONArray) parser.parse(dto.getSolution());

				List<ComponentDTO> componentList = new ArrayList<ComponentDTO>();
				// (type,data) ?????? ???????????? ??????. => type??? string / data??? string??? ?????????
				for (int i = 0; i < solutionJsonArray.size(); i++) {
					List<String> dataList = new ArrayList<String>();
					JSONObject json = (JSONObject) solutionJsonArray.get(i);
					String type = json.get("type").toString();
					String data = json.get("data").toString();
					ComponentDTO component = new ComponentDTO();

					if (type.equalsIgnoreCase("MULTIPLE_CHOICE_CORRECT_ANSWER")) {				// ????????? ????????? array of integer. ???????????? array of string.
						if (data.contains("[") && data.contains("]")) {
							if (data.contains(","))
								dataList = Arrays.asList(data.substring(1, data.length() - 1).split(","));
							else 
								dataList = Arrays.asList(data.substring(1, data.length() - 1));
						}
					} else {
						if (!data.equalsIgnoreCase("")) {
							// ??????????????? escape ?????? ????????? ??????.
							while (data.contains("\\/")) {
								data = data.replace("\\/", "/");
							}
							// DB ????????? ???????????? ??????????????? escape ????????? ????????? ?????? ?????? (response ?????? ??? \\\" ??? ??????????????? ??????)
							while (data.contains("\\\"")) {
								data = data.replace("\\\"", "\"");
							}
						}

						if (data.startsWith("[") && data.endsWith("]")) {
							// ["a", "b", "c", ... ] ??? ????????? ??????.
							dataList = Arrays.asList(data.substring(2, data.length()-2).split("\",\""));
							List<String> temp = new ArrayList<String>();
							if (type.contains("IMAGE")) {					// ????????? ???????????? : array of ????????? ?????????
								try {
									for (String img_name : dataList) {
										String sb = imgFileApi.getImgFileServiceComponent(new Long(probId), img_name);
										temp.add(sb);
									}
								} catch (FileNotFoundException e) {
									log.info("error : FileNotFoundException occurred. Cannot find the file path.");
								} catch (IOException e) {
									log.info("error : IOException occurred. Cannot load image file.");
								} finally {
									dataList = temp;
								}
							}
						} else
							dataList = new ArrayList<String>(Arrays.asList(data));
					}

					component.setType(type);
					component.setData(dataList);
					componentList.add(component);
				}
				solutionInfo.setSolution(componentList);
				solutionMap.put(dto.getProbID(), solutionInfo);
			} catch (ParseException e) {
				log.info("error: jsonParseException occurred.");
			} catch (ClassCastException e) {
				log.info("error: CastException occurred. cannot convert error.");
			}
		}
		return solutionMap;
	}

	public Map<String, Object> getSolutionMaterial(Integer problemID) {
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<Problem> problemOpt = problemRepo.findById(problemID);

		if (problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution", problem.getSolution());
			output.put("material", problem.getSource());

		} else {
			log.info("error : Problem NodataException occurred.");
		}
		return output;
	}

}
