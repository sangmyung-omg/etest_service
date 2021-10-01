package com.tmax.eTest.Contents.controller.problem;

import com.tmax.eTest.Contents.dto.problem.ProblemOutputDTO;
import com.tmax.eTest.Contents.service.ProblemServicesBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins="*")
@RestController
@RequestMapping(path = "/contents" + "/v1")
public class ProblemControllerV1 {

	@Autowired
	@Qualifier("ProblemServicesV1")
	ProblemServicesBase problemService;

    // 선택2) 문제 question 정보 json 파싱까지만 해서 각각 json 형식에 맞게 필드에 담아서 반환
	@GetMapping(value="/problems/{probId}/temp1")
	public ResponseEntity<Object> problem2(@PathVariable("probId") Integer probId) throws Exception{
		log.info("> problem info logic start! : " + Integer.toString(probId));

		ProblemOutputDTO result = problemService.getProblemInfo(probId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
