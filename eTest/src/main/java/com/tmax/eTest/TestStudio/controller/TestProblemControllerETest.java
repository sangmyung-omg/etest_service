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

import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.dto.problems.in.DeleteProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.GetProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PostTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagCurrStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutDiagProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutTestProbStatusDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.in.PutTestProblemDTOIn;
import com.tmax.eTest.TestStudio.dto.problems.out.GetTestProblemDTOOut;
import com.tmax.eTest.TestStudio.dto.problems.out.PostTestProblemDTOOut;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestProblemControllerETest {
	
	private final TestProblemApiComponentTs testProblemApiComponent;
	
	/**
	 * 등록
	 * new ResponseEntity<>(probGetResponse, HttpStatus.ACCEPTED);
	 */
	@PostMapping("/TestProblems")
	public 
			ResponseEntity<PostTestProblemDTOOut> 
//			ResponseEntity<String>
			CreateProblem(
			@RequestBody PostTestProblemDTOIn request
			) {
		
		try {
			
//			//0804s
//			GetProblemDTOOutProblems problems = 
//					new GetProblemDTOOutProblems("1000", "test","test","test","test","test","test",
//							10L,"test",new Date(20201020),"test",new Date(20201020),"test",new Date(20201020)
//							,"test","test","test","test");
//			List<GetProblemDTOOutProblems> LP = new ArrayList<GetProblemDTOOutProblems>();
//			LP.add(problems);
//			GetTestProblemDTOOut request = new GetTestProblemDTOOut( LP, "1" );
			//0804e
			List<Long> probIdList = new ArrayList<Long>();
			List<Long> skipIdxList = new ArrayList<Long>();
			List<Long> imgFailProbIdList = new ArrayList<Long>();
			List<List<Long>> result = testProblemApiComponent.CreateProblemComponet(request);
			
			probIdList=result.get(0);
			skipIdxList=result.get(1);
			imgFailProbIdList=result.get(2);
			List<String> probIdStrList = probIdList.stream().map(m->m.toString()).collect(Collectors.toList());
			String imgFailProbIdListToSring = imgFailProbIdList.toString();
			
//			if(skipIdxList.size()>0) {
//				if(imgFailProbIdList.size()>0) {
//					ProbPostResponse res = 
//							new ProbPostResponse(
//								"202","warning: 다음 idx의 문제생성 요청에 대한 문제 생성 실패 : "+ skipIdxList.toString()
//								+" (이유: 필수값이 없거나 한 문제에 담긴 reUK 또는 img또는 darkImg 가 50개이상, 이외의 문제는 생성 완료)"
//								+" 다음 문제의 일부/전체 이미지가 생성되지 않았습니다 : "+ imgFailProbIdListToSring
//								,probIdStrList
//								);
//					return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//				}else {
//					ProbPostResponse res = 
//							new ProbPostResponse(
//									"202","warning: 다음 idx의 문제생성 요청에 대한 문제 생성 실패 : "+ skipIdxList.toString()
//									+" (이유: 필수값이 없거나 한 문제에 담긴 reUK 또는 img또는 darkImg 가 50개이상, 이외의 문제는 생성 완료)"
//									,probIdStrList
//									); 
//					return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//				}
//			}else if(imgFailProbIdList.size()>0) {
//				ProbPostResponse res = new ProbPostResponse("202","warning: 문제생성성공, 그러나 다음 문제의 일부/전체 이미지가 생성되지 않았습니다 : "+ imgFailProbIdListToSring,probIdStrList); 
//				return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//			}
			
//			List<String> res =probIdStrList;
			PostTestProblemDTOOut res = new PostTestProblemDTOOut("success",probIdStrList);
//			String res = "success";
//					new GetProblemDTOOut("200","success",probIdStrList);
			return new ResponseEntity<>(res, HttpStatus.OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>( new PostTestProblemDTOOut( e.getMessage() ), HttpStatus.INTERNAL_SERVER_ERROR);
//			GetProblemDTOOut res = new GetProblemDTOOut("500","fail: errMessage: "+e.getMessage());
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	/**
	 * 조회
	 * 
	 */
	
	@GetMapping("/TestProblems")
	public ResponseEntity<GetTestProblemDTOOut> problems(
			@RequestBody GetProblemDTOIn request
			) {
		
		try {
//			System.out.println(request.getProbID());
			
			GetTestProblemDTOOut res = testProblemApiComponent.testProblemsGetComponent( request.getProbIDs() ) ;
			
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
	@PutMapping("/TestProblems")
	public ResponseEntity<String> updateProblems(
			@RequestBody PutTestProblemDTOIn request) {
		
		try {
			String res = testProblemApiComponent.updateProblemcomponent(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			return new ResponseEntity<>( "fail", HttpStatus.INTERNAL_SERVER_ERROR );	
		}
	}
	
	@PutMapping("/TestProbStatus")
	public ResponseEntity<String> updateTestProbStatus(
			@RequestBody PutTestProbStatusDTOIn request) throws Exception {
		
		try {
			String res = testProblemApiComponent.updateTestProbStatus(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			throw e;
//			 e.printStackTrace(); 
//			return new ResponseEntity<>( "fail"+", "+"errorMessage: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );	
		}
	}
	
	/**
	 * 삭제
	 * 
	 */
	@DeleteMapping("/TestProblems")
	public ResponseEntity<String> delete(@RequestBody DeleteProblemDTOIn request) {
		
		try {
			String res = testProblemApiComponent.deleteProbComponent(request.getUserID(), request.getProbIDs() );

				return new ResponseEntity<>( res, HttpStatus.OK );

			
		}catch(Exception e) {
			 e.printStackTrace(); 
			return new ResponseEntity<>( "fail", HttpStatus.INTERNAL_SERVER_ERROR );	
		}
	}

}
