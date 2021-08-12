package com.tmax.eTest.TestStudio.controller;

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

import com.tmax.eTest.TestStudio.controller.component.DiagProblemApiComponentETest;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentETest;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiagProblemControllerETest {
	
	private final DiagProblemApiComponentETest diagProblemApiComponent;
	
	/**
	 * 조회
	 * 
	 */
	
	@GetMapping("/DiagProblems")
	public ResponseEntity<GetDiagProblemDTOOut> problems(
			@RequestBody GetProblemDTOIn request
			) {
		
		try {
//			System.out.println(request.getProbID());
			
			GetDiagProblemDTOOut res = diagProblemApiComponent.diagProblemsGetComponent( request.getProbID() );
			
			return new ResponseEntity<>( res, HttpStatus.OK );
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}
	
	
	/**
	 * 수정
	 * 
	 */
	@PutMapping("/DiagProblems")
	public ResponseEntity<String> updateProblems(
			@RequestBody PutDiagProblemDTOIn request) {
		
		try {
			String res = diagProblemApiComponent.updateProblemcomponent(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			return new ResponseEntity<>( "fail", HttpStatus.INTERNAL_SERVER_ERROR );	
		}
	}
	
	@PutMapping("/DiagCurrStatus")
	public ResponseEntity<String> updateDiagCurrStatus(
			@RequestBody PutDiagCurrStatusDTOIn request) {
		
		try {
			String res = diagProblemApiComponent.updateDiagCurrStatus(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			return new ResponseEntity<>( "fail", HttpStatus.INTERNAL_SERVER_ERROR );	
		}
	}
	

}
