package com.tmax.eTest.Contents.controller; 

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Contents.dto.problem.DiagnosisProblemDTO;
import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.dto.problem.TestProblemDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.model.DiagnosisProblemBody;
import com.tmax.eTest.Contents.model.TestProblemBody;
import com.tmax.eTest.Contents.service.ProblemServices;


@RestController
public class ProblemController {
	
	@Autowired
	ProblemServices problemService;
	
	
	@GetMapping(value="/problems/{id}")
	public ResponseEntity<ProblemDTO> problem(@PathVariable("id") long id) throws Exception{
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseEntity<ProblemDTO> output;
		try {
			ProblemDTO body = problemService.getProblem(id);
			body.setMessage("success");
			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
		}catch(NoDataException e) {
			ProblemDTO body = new ProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			ProblemDTO body = new ProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		}
		
		return output;
	}
	
	@GetMapping(value="/problems/test")
	public ResponseEntity<TestProblemDTO> testProblem(@RequestBody TestProblemBody requestBody) throws Exception{
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseEntity<TestProblemDTO> output;
        Integer newIndex = requestBody.getIndex().orElseGet(()->0);
        
		try {
			TestProblemDTO body = new TestProblemDTO(problemService.getTestProblem(requestBody.getSetNum(), newIndex));
			
			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
		}catch(NoDataException e) {
			TestProblemDTO body = new TestProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			TestProblemDTO body = new TestProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		}
		
		return output;
	}
	
	@GetMapping(value= "/problems/diagnosis")
	public ResponseEntity<DiagnosisProblemDTO> diagnosisProblem(@RequestBody DiagnosisProblemBody requestBody) throws Exception{
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseEntity<DiagnosisProblemDTO> output;
        
        try {
        	DiagnosisProblemDTO body = new DiagnosisProblemDTO(problemService.getDiagnosisProblem(requestBody.getSetNum()));
			
			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
		}catch(NoDataException e) {
			DiagnosisProblemDTO body = new DiagnosisProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			DiagnosisProblemDTO body = new DiagnosisProblemDTO();
			body.setMessage("Failed: "+e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		}
		
		return output;
	}
}
