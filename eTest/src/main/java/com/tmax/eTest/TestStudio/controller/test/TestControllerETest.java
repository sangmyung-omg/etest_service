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
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemSetDTO;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PostTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryTs;
import com.tmax.eTest.TestStudio.service.DiagProblemServiceTs;
import com.tmax.eTest.TestStudio.service.ProbChoiceServiceTs;
import com.tmax.eTest.TestStudio.service.ProbUKRelServiceTs;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestControllerETest {
	
	private final TestProblemApiComponentTs problemApiComponent;
	private final DiagProblemServiceTs diagProblemServiceETest;
	private final TestProblemQRepositoryETest tempTestRepository; 
	private final ProbChoiceServiceTs probChoiceServiceETest;
	private final ProbUKRelServiceTs probUKRelServiceETest;
	private final ProbChoiceQRepositoryTs probChoiceQRepositoryETest;
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
	
	/**
	 * 
	 * qdsl
	 */
	@GetMapping("/qdsl1")
	public ResponseEntity<String> qdslTest(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			
			String output = tempTestRepository.searchLatestProblem().getProbID().toString();
			
			return new ResponseEntity<>(output, HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@GetMapping("/qdsl2")
	public ResponseEntity<String> qdslTest2(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			
			String output = tempTestRepository.searchLatestProblem().getProbID().toString();
			
			return new ResponseEntity<>(output, HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@GetMapping("/qdsl3")
	public ResponseEntity<String> qdslTest3(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			
			String output = tempTestRepository.searchLatestProblemIDTest().toString();
			
			return new ResponseEntity<>(output, HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//problemchoice
	@GetMapping("/pbCD")
	public ResponseEntity<String> probChoiceDelTest(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			Long output = probChoiceQRepositoryETest.probChoiceDeleteByProbId(2000);
			
			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//
	@GetMapping("/pbCG")
	public ResponseEntity<String> probChoiceGetTest(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			List<ProblemChoice> result = probChoiceServiceETest.findAllByProbId(1001L);
			List<String> output = new ArrayList<String>();
			for(ProblemChoice pc : result ) {
				output.add( String.valueOf( pc.getChoiceNum() ) );
				System.out.println(pc.getChoiceNum());
			}
			
			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@PutMapping("/pbCU")
	public ResponseEntity<String> probChoicePutTest(
			@RequestBody PostTestProblemDTOIn request
			) {
		
		try {
			probChoiceServiceETest.probChoiceUpdate("testuser", request.getTestProblems().get(0).getProbChoices(),2019L);
			List<String> output = new ArrayList<String>();
			
			
			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@GetMapping("/pbUKRG")
	public ResponseEntity<String> probUKRGetTest(
//			@RequestBody CreateDiagProblemRequest request
			) {
		
		try {
			
			List<ProblemUKRelation> result = probUKRelServiceETest.findAllByProbId(1001L);
			List<String> output = new ArrayList<String>();
			for(ProblemUKRelation pc : result ) {
				output.add( String.valueOf( pc.getUkId() ) );
				System.out.println(pc.getUkId());
			}
			
			return new ResponseEntity<>(output.toString(), HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
