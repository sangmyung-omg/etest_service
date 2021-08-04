package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.repository.problem.DiagnosisProblemRepo;
import com.tmax.eTest.TestStudio.dto.SelfProblemListDTO;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SelfController {

	private final DiagnosisProblemRepo diagnosisProblemRepo;

	@GetMapping("test-studio/problems/self")
	@ResponseBody
	public ResponseEntity<List<SelfProblemListDTO>> SelfProblemList() {
		try {
			List<DiagnosisProblem> query = diagnosisProblemRepo.findAll();
			List<SelfProblemListDTO> body = query.stream().map(m -> new SelfProblemListDTO(
					m.getProbId(),
					m.getOrderNum(),
					m.getProblem().getDifficulty()
					)).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}