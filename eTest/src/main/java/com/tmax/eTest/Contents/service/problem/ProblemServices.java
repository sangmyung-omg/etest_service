package com.tmax.eTest.Contents.service.problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.model.ProblemChoice;
import com.tmax.eTest.Contents.model.Problem;
import com.tmax.eTest.Contents.repository.DiagnosisProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.Contents.repository.ProblemUKRelRepository;
import com.tmax.eTest.Contents.repository.TestProblemRepository;
import com.tmax.eTest.Test.repository.UkRepository;


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
		Optional<Problem> problemOpt = problemRepo.findById(problemID);
		
		if(problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			Map<Long, String> choices = new HashMap<Long, String>();
			output.put("type", problem.getAnswerType());
			output.put("question", problem.getQuestion());
			output.put("passage", null);
			output.put("preface", null);
			output.put("difficulty", problem.getDifficulty());
			List<ProblemChoice> choiceList = probChoiceRepo.findAllByProbID(problem);
			for(ProblemChoice choice:choiceList) {
				choices.put(choice.getChoiceNum(), choice.getText());
			}
			output.put("choices", choices);
			
		}else {
			throw new NoDataException(problemID);
		}
		return output;
	}
}

