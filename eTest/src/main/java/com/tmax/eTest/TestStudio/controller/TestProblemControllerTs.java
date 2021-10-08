package com.tmax.eTest.TestStudio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.tmax.eTest.TestStudio.controller.component.TestProblemApiComponentTs;
import com.tmax.eTest.TestStudio.controller.component.exception.CustomExceptionTs;
import com.tmax.eTest.TestStudio.controller.component.exception.ErrorCodeEnumTs;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class TestProblemControllerTs {
	
	private final TestProblemApiComponentTs testProblemApiComponent;
	
	/**
	 * 등록
	 * new ResponseEntity<>(probGetResponse, HttpStatus.ACCEPTED);
	 * @throws ParseException 
	 * @throws NoDataExceptionTs 
	 * @throws Exception 
	 */
	@PostMapping("/test-studio/TestProblems")
	public 
			ResponseEntity<PostTestProblemDTOOut> 
//			ResponseEntity<String>
			CreateProblem(
			@RequestBody PostTestProblemDTOIn request
			) throws IOException, NoDataExceptionTs, ParseException {

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
			
	}
	
	
	
	/**
	 * 조회
	 * 
	 */
	
//	@GetMapping("/TestProblems")
	@GetMapping(value="/test-studio/TestProblems/{userID}/{probIDs}")
	public ResponseEntity<GetTestProblemDTOOut> problems (
//			@RequestBody GetProblemDTOIn request
			@PathVariable("userID") String userID,
			@PathVariable("probIDs") String probIDs
			) throws Exception {
			
//			if(request.getProbIDs().isEmpty()) {
			if(probIDs == null) {
				throw new CustomExceptionTs(ErrorCodeEnumTs.INVALID_REQUEST_INPUT);
			}
			GetTestProblemDTOOut res = testProblemApiComponent.testProblemsGetComponent( 
//					request.getProbIDs()
					probIDs
					) ;

			return new ResponseEntity<>( res, HttpStatus.OK );
			
	}
	
	/**
	 * 수정 
	 * 
	 */
	@PutMapping("/test-studio/TestProblems")
	public ResponseEntity<String> updateProblems(
			@RequestBody PutTestProblemDTOIn request) throws Exception {
		
			String res = testProblemApiComponent.updateProblemcomponent(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

			
	}
	
	@PutMapping("/test-studio/TestProbStatus")
	public ResponseEntity<String> updateTestProbStatus(
			@RequestBody PutTestProbStatusDTOIn request) throws Exception {
		
			String res = testProblemApiComponent.updateTestProbStatus(request);

				return new ResponseEntity<>( res, HttpStatus.OK );

	}
	
	/**
	 * 삭제
	 * 
	 */
	@DeleteMapping("/test-studio/TestProblems")
	public ResponseEntity<String> delete(@RequestBody DeleteProblemDTOIn request) throws Exception {
		
			String res = testProblemApiComponent.deleteProbComponent(request.getUserID(), request.getProbIDs() );

				return new ResponseEntity<>( res, HttpStatus.OK );

			
	}

}
