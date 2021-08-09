package com.tmax.eTest.TestStudio.controller.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentETest;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceETest;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestControllerETest {
	
	private final TestProblemApiComponentETest problemApiComponent;
	private final DiagProblemServiceETest diagProblemServiceETest;
	
	/**
	 * 등록
	 * new ResponseEntity<>(probGetResponse, HttpStatus.ACCEPTED);
	 */
	@PostMapping("/ETest1")
	public ResponseEntity<String> CreateProblem(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			System.out.println("s");
			
			DiagnosisProblem diagnosisProblem = diagProblemServiceETest.findByIdOrderSorted(1L).get(0); 
			System.out.println ( diagnosisProblem.getOrderNum() );
			diagnosisProblem = diagProblemServiceETest.findByIdOrderSorted(1L).get(1); 
			System.out.println ( diagnosisProblem.getOrderNum() );
			
			
			
			return new ResponseEntity<>("res", HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	

	

}
