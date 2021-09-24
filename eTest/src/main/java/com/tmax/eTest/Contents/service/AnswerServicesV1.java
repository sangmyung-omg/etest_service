package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.dto.answer.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.dto.answer.Temp1SolutionDTO;
import com.tmax.eTest.Contents.dto.problem.ComponentDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.util.ImgFileUtils;
import com.tmax.eTest.LRS.dto.StatementDTO;

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
	ImgFileUtils imgfileUtils;
	
	public Integer evaluateIfCorrect(Integer probId, List<StatementDTO> lrsbody) throws Exception {
		String userAnswer = lrsbody.get(0).getUserAnswer();
		log.info("Problem ID : " + Integer.toString(probId) + ", user answer : " + userAnswer);
		// log.info("Inserted LRS body : " + lrsbody.toString());

		Map<String, Object> data = new HashMap<String, Object>();

		try {
			data = getProblemSolution(probId);
		} catch (Exception e) {
			log.info(e.toString());
			return 0;
		}
		String inputString = data.get("solution").toString();
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(inputString);
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
				// log.info("correctAnswer : " + correctAnswer);
			}
			// log.info("type : " + type);
		}
		// log.info("Total solution string : " + inputString);
		log.info("Correct Answer is... " + correctAnswer);
		if (correctAnswer.equalsIgnoreCase(userAnswer)) return 1;
		else return 0;
	}

	public Map<String, Object> getProblemSolution(Integer problemID) throws Exception {
		Map<String, Object> output = new HashMap<String, Object>();

		log.info("Getting solution info......");
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

	public Map<Integer, Temp1SolutionDTO> getParsedMultipleSolutions(List<Integer> probIdList) {
		log.info("Getting multiple solution infos......");
		List<Problem> probList = problemRepo.findByProbIDIn(probIdList);
		Map<Integer, Temp1SolutionDTO> solutionMap = new HashMap<Integer, Temp1SolutionDTO>();
		for (Problem dto : probList) {
			Temp1SolutionDTO solutionInfo = new Temp1SolutionDTO();
			int probId = dto.getProbID();
			solutionInfo.setProbId(probId);
			solutionInfo.setMaterial(dto.getSource());
			// solutionInfo.setSolution(dto.getSolution());

			// Prasing the solution column
			try {
				JSONParser parser = new JSONParser();
				JSONArray solutionJsonArray = (JSONArray) parser.parse(dto.getSolution());

				List<ComponentDTO> componentList = new ArrayList<ComponentDTO>();
				// (type,data) 쌍의 리스트로 구성. => type은 string / data는 string의 리스트
				for (int i = 0; i < solutionJsonArray.size(); i++) {
					List<String> dataList = new ArrayList<String>();
					JSONObject json = (JSONObject) solutionJsonArray.get(i);
					String type = json.get("type").toString();
					String data = json.get("data").toString();
					ComponentDTO component = new ComponentDTO();

					if (type.equalsIgnoreCase("MULTIPLE_CHOICE_CORRECT_ANSWER")) {				// 객관식 정답만 array of integer. 나머지는 array of string.
						if (data.contains("[") && data.contains("]")) {
							if (data.contains(","))
								dataList = Arrays.asList(data.substring(1, data.length() - 1).split(","));
							else 
								dataList = Arrays.asList(data.substring(1, data.length() - 1));
						}
					} else {
						if (!data.equalsIgnoreCase("")) {
							// 슬래시에도 escape 문자 붙어서 처리.
							while (data.contains("\\/")) {
								data = data.replace("\\/", "/");
							}
							// DB 데이터 자체에는 쌍따옴표에 escape 없어야 되는데 있는 경우 (response 전달 시 \\\" 로 표시되어서 오류)
							while (data.contains("\\\"")) {
								data = data.replace("\\\"", "\"");
							}
						}

						if (data.startsWith("[") && data.endsWith("]")) {
							// ["a", "b", "c", ... ] 의 형태라 가정.
							dataList = Arrays.asList(data.substring(2, data.length()-2).split("\",\""));
							List<String> temp = new ArrayList<String>();
							if (type.contains("IMAGE")) {
								for (String img_name : dataList) {
									String sb = imgfileUtils.getImgFileServiceComponent(probId, img_name);
									temp.add(sb);
								}
								dataList = temp;
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
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		log.info(solutionMap.toString());
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
