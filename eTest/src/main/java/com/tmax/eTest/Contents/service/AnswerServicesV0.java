package com.tmax.eTest.Contents.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Contents.dto.answer.CustomizedSolutionDTO;
import com.tmax.eTest.Contents.dto.answer.SolutionDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.repository.ProblemChoiceRepository;
import com.tmax.eTest.Contents.repository.ProblemRepository;
import com.tmax.eTest.LRS.dto.StatementDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("AnswerServicesV0")
public class AnswerServicesV0 implements AnswerServicesBase {

	@Autowired
	ProblemRepository problemRepo;

	@Autowired
	ProblemChoiceRepository probChoiceRepo;

	public Map<String, Object> getProblemSolution(Integer problemID) {
		Map<String, Object> output = new HashMap<String, Object>();
		Optional<Problem> problemOpt = problemRepo.findById(problemID);

		if (problemOpt.isPresent()) {
			Problem problem = problemOpt.get();
			output.put("solution", problem.getSolution());

		} else {
			log.info("error : Problem NoDataException occurred.");

		}
		return output;
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

	// Not used in previous versions
	public Integer evaluateIfCorrect(Integer probId, List<StatementDTO> lrsbody) {
		return 0;
	}

	public Map<Integer, CustomizedSolutionDTO> getMultipleSolutions(List<Integer> probIdList) {
		return null;
	}

	public Map<Integer, SolutionDTO> getParsedMultipleSolutions(List<Integer> probIdLisT) {
		return null;
	}
}
