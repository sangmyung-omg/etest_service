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

import com.tmax.eTest.TestStudio.controller.component.DiagProblemApiComponentTs;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DiagProblemControllerTs {
	
	private final DiagProblemApiComponentTs diagProblemApiComponent;
	
	/**
	 * 조회
	 * 
	 */
	
	@GetMapping("/DiagProblems")
	public ResponseEntity<GetDiagProblemDTOOut> problems(
			@RequestBody GetProblemDTOIn request
			) throws Exception {
		
		try {
//			System.out.println(request.getProbID());
			
			GetDiagProblemDTOOut res = diagProblemApiComponent.diagProblemsGetComponent( request.getProbIDs() );
			
			return new ResponseEntity<>( res, HttpStatus.OK );
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * 수정
	 * @throws Exception 
	 * 
	 */
	@PutMapping("/DiagProblems")
	public ResponseEntity<String> updateProblems(
			@RequestBody PutDiagProblemDTOIn request) throws Exception {
		
		try {
			String res = diagProblemApiComponent.updateProblemcomponent(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			 throw e;
		}
	}
	
	@PutMapping("/DiagCurrStatus")
	public ResponseEntity<String> updateDiagCurrStatus(
			@RequestBody PutDiagCurrStatusDTOIn request) throws Exception {
		
		try {
			String res = diagProblemApiComponent.updateDiagCurrStatus(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			 throw e;	
		}
	}
	

}