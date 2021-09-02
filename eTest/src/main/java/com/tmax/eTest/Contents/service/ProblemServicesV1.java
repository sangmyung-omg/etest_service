package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.error_report.ErrorReport;
import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.dto.problem.Temp0ProblemOutputDTO;
import com.tmax.eTest.Contents.dto.problem.ComponentDTO;
import com.tmax.eTest.Contents.dto.problem.Temp1ProblemOutputDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.exception.problem.UnavailableTypeException;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.ErrorReportRepository;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemUKRelRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;
import com.tmax.eTest.Test.repository.UkRepository;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Slf4j
@Service("ProblemServicesV1")
public class ProblemServicesV1 implements ProblemServicesBase {
	@Autowired
	DiagnosisProblemRepository diagProbRepo;
	
	@Autowired
	ProblemChoiceRepository probChoiceRepo;
	
	@Autowired
	ProblemRepository problemRepo;
	
	@Autowired
	ProblemUKRelRepository probUKRelRepo;
	
	@Autowired
	TestProblemRepository testProblemRepo;
	
	@Autowired
	UkRepository uKMasterRepo;
	
	@Autowired
	ErrorReportRepository errorRepo; 
	
	public Temp1ProblemOutputDTO getProblemInfo(Integer probId) throws Exception{
		Temp1ProblemOutputDTO output = new Temp1ProblemOutputDTO();

		log.info("Getting problem info......");
		Problem problemInfo = problemRepo.findById(probId).orElseThrow(() -> new NoDataException(probId));

		// 먼저 담을 수 있는 문제 정보 담기
		output.setAnswerType(problemInfo.getAnswerType());
		output.setDifficulty(problemInfo.getDifficulty());

		// 문제를 구성하는 각 컴포넌트 정보 담기 (1 컴포넌트 당 1 json)
		JSONParser parser = new JSONParser();
		JSONArray questionJsonArray = (JSONArray) parser.parse(problemInfo.getQuestion());
		List<ComponentDTO> componentList = new ArrayList<ComponentDTO>();
		for (int i=0; i < questionJsonArray.size(); i++) {
			JSONObject json = (JSONObject) questionJsonArray.get(i);
			ComponentDTO component = new ComponentDTO();
			component.setData(json.get("data").toString());
			component.setType(json.get("type").toString());
			componentList.add(component);
		}

		output.setComponents(componentList);
		output.setMessage("Successfully returned");

		return output;
	}

	public Temp0ProblemOutputDTO getParsedProblemInfo(Integer probId) throws Exception {
		
		Temp0ProblemOutputDTO output = new Temp0ProblemOutputDTO();
		
		log.info("Getting problem info......");
		Problem problemInfo = problemRepo.findById(probId).orElseThrow(() -> new NoDataException(probId));
		problemInfo.getProblemChoices();
		output.setAnswerType(problemInfo.getAnswerType());
		output.setDifficulty(problemInfo.getDifficulty());

		JSONParser parser = new JSONParser();
		JSONArray questionJsonArray = (JSONArray) parser.parse(problemInfo.getQuestion());
		
		// output ingredients
		List<String> componentTypeList = new ArrayList<String>();
		List<String> questionText = new ArrayList<String>();
		List<String> exampleBoxText = new ArrayList<String>();
		List<String> multipleChoiceText = new ArrayList<String>();
		List<String> questionImage = new ArrayList<String>();
		List<String> multipleChoiceImage = new ArrayList<String>();
		List<String> exampleBoxImage = new ArrayList<String>();
		String preface = "";
		Integer shortAnswerText = 0;

		// Preprocessing
		for (int i=0; i < questionJsonArray.size(); i++) {
			JSONObject json = (JSONObject) questionJsonArray.get(i);
			String type = json.get("type").toString();
			String data = json.get("data").toString();
			
			componentTypeList.add(type);
			if (type.equalsIgnoreCase("QUESTION_TEXT")) questionText.add(data);
			else if (type.equalsIgnoreCase("EXAMPLE_BOX_TEXT")) exampleBoxText.add(data);
			else if (type.equalsIgnoreCase("MULTIPLE_CHOICE_TEXT")) {
				log.info(data);
				// logger.info(json.get("data"));
				try {
					multipleChoiceText = (List<String>) json.get("data");
				} catch (Exception e) {
					log.info("MULTIPLE_CHOICE_TEXT Parsing error : " + e.getMessage());
					multipleChoiceText.add("MULTIPLE_CHOICE_TEXT Parsing error");
					multipleChoiceText.add(data);
				}

				if (json.containsKey("preface"))
					preface = json.get("preface").toString();
				
			}
			else if (type.equalsIgnoreCase("QUESTION_IMAGE")) questionImage.add(data);
			else if (type.equalsIgnoreCase("MULTIPLE_CHOICE_IMAGE")) {
				multipleChoiceImage.add(data);
				if (json.containsKey("preface"))
					preface = json.get("preface").toString();
			}
			else if (type.equalsIgnoreCase("EXAMPLE_BOX_IMAGE")) exampleBoxText.add(data);

		}

		// insert into output
		output.setComponentTypeList(componentTypeList);
		output.setQuestionText(questionText);
		output.setExampleBoxText(exampleBoxText);
		output.setMultipleChoiceText(multipleChoiceText);
		output.setQuestionImage(questionImage);
		output.setMultipleChoiceImage(multipleChoiceImage);
		output.setExampleBoxImage(exampleBoxImage);
		output.setPreface(preface);
		output.setShortAnswerText(shortAnswerText);

		output.setMessage("Successfully returned!");
		return output;
	}


	public List<Integer> getDiagnosisProblem(String setNum) throws Exception{
		List<Integer> output = new ArrayList<Integer>(); 

		List<DiagnosisProblem> diagnosisProblems = diagProbRepo.findDiagnosisProblems(setNum);
		if(diagnosisProblems.size()==0) {
			throw new NoDataException(setNum);
		}else {
			for(DiagnosisProblem d : diagnosisProblems) {
				output.add(d.getProbId());
			}
		}
		return output;
	}
	public String insertErrorReport(long problemID, String id, String reportType, String reportText) throws Exception{
		final String[] AVAILABLE_ERROR_TYPE= {"QUESTION_ERROR", "PASSAGE_ERROR"};
		ErrorReport errorReport = new ErrorReport();
		errorReport.setProbID(problemID);
		errorReport.setUserUUID(id);
		
		//ReportType은 "QUESTION_ERROR"와 "PASSAGE_ERROR" 둘 중 하나만 들어와야됨
		if(Arrays.stream(AVAILABLE_ERROR_TYPE).anyMatch(reportType::equals)) {
			errorReport.setReportType(reportType);
		}else {
			throw new UnavailableTypeException(AVAILABLE_ERROR_TYPE, reportType);
		}
		errorReport.setReportText(reportText);
		
		try {
			errorRepo.save(errorReport);
		}catch(Exception e) {
			throw e;
		}
		return "success";
	}

	// Not using
	public ProblemDTO getProblem(Integer probId) throws Exception{
		ProblemDTO output = new ProblemDTO();
		return output;
	}
}

