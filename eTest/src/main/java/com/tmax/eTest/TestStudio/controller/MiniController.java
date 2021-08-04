package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.Common.repository.problem.TestProblemRepo;
import com.tmax.eTest.TestStudio.dto.MiniProblemListDTO;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MiniController {

	private final TestProblemRepo testProblemRepo;

	@GetMapping("test-studio/problems/mini")
	@ResponseBody
	public ResponseEntity<List<MiniProblemListDTO>> PartList() {
		try {
			List<TestProblem> query = testProblemRepo.findAll();
			List<MiniProblemListDTO> body = query.stream().map(m -> new MiniProblemListDTO(
					m.getProbID(),
					ObjectUtils.isEmpty(m.getPart()) ? null : m.getPart().getPartName(),
					m.getProblem().getDifficulty(),
					m.getSubject(),
					m.getStatus(),
					m.getProblem().getQuestion(),
					m.getProblem().getEditDate()
					)).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}