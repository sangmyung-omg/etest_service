package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.model.ProblemChoice;
import com.tmax.eTest.Contents.model.TestProblem;
import com.tmax.eTest.Contents.model.DiagnosisProblem;
import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemUKRelRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;
import com.tmax.eTest.Test.repository.UkRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;

@Service
public class ProblemServices {
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
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ProblemDTO getProblem(Integer problemID) throws Exception{
		ProblemDTO output;
		Optional<Problem> problemOpt = problemRepo.findById(problemID);
		
		if(problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			Map<Long, String> choices = new HashMap<Long, String>();
			String type = problem.getAnswerType();
//			String questionJsonString = problem.getQuestion();
//			JsonObject questionJson= JsonParser.parseString(questionJsonString).getAsJsonObject();

			
//			String question = questionJson.get("question").toString().replaceAll("\"", "");
//			String passage = questionJson.get("passage").toString().replaceAll("\"", "");
//			String preface = questionJson.get("preface").toString().replaceAll("\"", "");
	        String question = problem.getQuestion();
			String passage = "";
			String preface = "";
			
			
			String difficulty = problem.getDifficulty();
			
			List<ProblemChoice> choiceList = probChoiceRepo.findAllByProbID(problem);
			for(ProblemChoice choice:choiceList) {
				choices.put(choice.getChoiceNum(), choice.getText());
			}
			output = new ProblemDTO(type, question, passage, preface, difficulty, choices);
			
		}else {
			throw new NoDataException(problemID);
		}
		
		return output;
	}

	public List<Integer> getTestProblem(int setNum, int index) throws Exception{
		List<Integer> output = new ArrayList<Integer>(); 

		index = Math.max(0, index);
		List<TestProblem> testProblems = testProblemRepo.findSetProblems(setNum, index);
	
		if(testProblems.size()==0) {
			throw new NoDataException(setNum);
		}else {
			for(TestProblem t : testProblems) {
				output.add(t.getProbID());
			}
		}
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
}

