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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Contents.controller.ArticleController;
import com.tmax.eTest.TestStudio.controller.component.DiagProblemApiComponentTs;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetDiagProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class DiagProblemControllerTs {
	
	private final DiagProblemApiComponentTs diagProblemApiComponent;
	
	/**
	 * 조회
	 * 
	 */
	
//	@GetMapping("/DiagProblems")
	@GetMapping(value="/test-studio/DiagProblems/{userID}/{probIDs}")
	public ResponseEntity<GetDiagProblemDTOOut> problems(
//			@RequestBody GetProblemDTOIn request
			@PathVariable("userID") String userID,
			@PathVariable("probIDs") String probIDs
			) throws Exception {
		
//			System.out.println(request.getProbID());
			
			GetDiagProblemDTOOut res = diagProblemApiComponent.diagProblemsGetComponent( 
//					request.getProbIDs() 
					probIDs
					);
			
			return new ResponseEntity<>( res, HttpStatus.OK );
			
	}
	
	
	/**
	 * 수정
	 * @throws Exception 
	 * 
	 */
	@PutMapping("/test-studio/DiagProblems")
	public ResponseEntity<String> updateProblems(
			@RequestBody PutDiagProblemDTOIn request) throws Exception {
		
			String res = diagProblemApiComponent.updateProblemcomponent(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
	}
	
	@PutMapping("/test-studio/DiagCurrStatus")
	public ResponseEntity<String> updateDiagCurrStatus(
			@RequestBody PutDiagCurrStatusDTOIn request) throws Exception {
		
			String res = diagProblemApiComponent.updateDiagCurrStatus(request);

				return new ResponseEntity<>( res, HttpStatus.OK );
		
	}
	

}
