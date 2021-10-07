package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.TestStudio.dto.SelfProblemListDTO;
import com.tmax.eTest.TestStudio.repository.SelfRepository;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SelfController {

	private final SelfRepository selfRepository;

	@GetMapping("test-studio/problems/self")
	public ResponseEntity<List<SelfProblemListDTO>> SelfProblemList(@RequestParam(value = "curriculumId") Integer curriculumId) {
		return new ResponseEntity<>(selfRepository.findByCurriculum_CurriculumId(curriculumId).stream().map(SelfProblemListDTO::new).collect(Collectors.toList()), HttpStatus.OK);
	}

}