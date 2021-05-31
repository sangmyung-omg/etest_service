package com.tmax.eTest.Contents.service.problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.dao.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.dao.ProblemChoiceDAO;
import com.tmax.eTest.Contents.dao.ProblemChoiceRepository;
import com.tmax.eTest.Contents.dao.ProblemDAO;
import com.tmax.eTest.Contents.dao.ProblemRepository;
import com.tmax.eTest.Contents.dao.ProblemUKRelRepository;
import com.tmax.eTest.Contents.dao.TestProblemRepository;

import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.dao.UkRepository;

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
	
	public Map<String, Object> getProblem(long problemID) throws Exception{
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<ProblemDAO> problemOpt = problemRepo.findById(problemID);
		
		if(problemOpt.isPresent()) {
			ProblemDAO problem = problemOpt.get();
			Map<Long, String> choices = new HashMap<Long, String>();
			output.put("type", problem.getAnswerType());
			output.put("question", problem.getQuestion());
			output.put("passage", null);
			output.put("preface", null);
			output.put("difficulty", problem.getDifficulty());
			List<ProblemChoiceDAO> choiceList = probChoiceRepo.findAllByProbID(problem);
			for(ProblemChoiceDAO choice:choiceList) {
				choices.put(choice.getChoiceNum(), choice.getText());
			}
			output.put("choices", choices);
			
		}else {
			throw new NoDataException(problemID);
		}
		return output;
	}
}

