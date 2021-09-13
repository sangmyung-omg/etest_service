package com.tmax.eTest.Contents.controller.problem; 

import java.nio.charset.Charset;

import com.tmax.eTest.Common.model.error_report.ErrorReportBody;
import com.tmax.eTest.Common.model.problem.DiagnosisProblemBody;
import com.tmax.eTest.Contents.dto.problem.DiagnosisProblemDTO;
import com.tmax.eTest.Contents.dto.problem.ErrorDTO;
import com.tmax.eTest.Contents.dto.problem.Temp0ProblemOutputDTO;
import com.tmax.eTest.Contents.dto.problem.Temp1ProblemOutputDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.exception.problem.UnavailableTypeException;
import com.tmax.eTest.Contents.service.ProblemServicesBase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	// 선택1) 문제 question 정보 풀어해쳐서 각각 필드에 담아서 반환
	@GetMapping(value="/problems/{probId}/temp0")
	public ResponseEntity<Object> problem1(@PathVariable("probId") Integer probId) throws Exception{
		log.info("> problem1 info logic start! : " + Integer.toString(probId));
		Temp0ProblemOutputDTO result = new Temp0ProblemOutputDTO();
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

		try {
			result = problemService.getParsedProblemInfo(probId);
			
			result.setMessage("Successfully returned");
			log.info(result.toString());
			return new ResponseEntity<>(result, headers, HttpStatus.OK);
		}catch(NoDataException e) {
			result = new Temp0ProblemOutputDTO();
			result.setMessage("error : "+e.getMessage());
			return new ResponseEntity<>(result, headers, HttpStatus.BAD_REQUEST);
		}catch(Exception e) {
			result = new Temp0ProblemOutputDTO();
			result.setMessage("error : "+e.getMessage());
			return new ResponseEntity<>(result, headers, HttpStatus.NOT_FOUND);
		}
	}

    // 선택2) 문제 question 정보 json 파싱까지만 해서 각각 json 형식에 맞게 필드에 담아서 반환
	@GetMapping(value="/problems/{probId}/temp1")
	public ResponseEntity<Object> problem2(@PathVariable("probId") Integer probId) throws Exception{
		log.info("> problem2 info logic start! : " + Integer.toString(probId));
		// HttpHeaders headers= new HttpHeaders();
        // headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

		Temp1ProblemOutputDTO result = problemService.getProblemInfo(probId);
		log.info(result.toString());
		return new ResponseEntity<>(result, HttpStatus.OK);

		// try {
		// 	ProblemDTO body = problemService.getProblem(id);
			
		// 	body.setMessage("success");
		// 	// return new ResponseEntity<>(body, headers, HttpStatus.OK);
		// 	return new ResponseEntity<>(body, HttpStatus.OK);
		// }catch(NoDataException e) {
		// 	ProblemDTO body = new ProblemDTO();
		// 	body.setMessage("Failed: "+e.getMessage());
		// 	// return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		// 	return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		// }catch(Exception e) {
		// 	ProblemDTO body = new ProblemDTO();
		// 	body.setMessage("Failed: "+e.getMessage());
		// 	// return new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		// 	return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
		// }
	}
	
//	@GetMapping(value="/problems/test")
//	public ResponseEntity<TestProblemDTO> testProblem(@RequestBody TestProblemBody requestBody) throws Exception{
//		HttpHeaders headers= new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//        ResponseEntity<TestProblemDTO> output;
//        Integer newIndex = requestBody.getIndex().orElseGet(()->0);
//        
//		try {
//			TestProblemDTO body = new TestProblemDTO(problemService.getTestProblem(requestBody.getSubject(), newIndex));
//			
//			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
//		}catch(NoDataException e) {
//			TestProblemDTO body = new TestProblemDTO();
//			body.setMessage("Failed: "+e.getMessage());
//			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
//		}catch(Exception e) {
//			TestProblemDTO body = new TestProblemDTO();
//			body.setMessage("Failed: "+e.getMessage());
//			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
//		}
//		
//		return output;
//	}
	
	@GetMapping(value= "/problems/diagnosis")
	public ResponseEntity<DiagnosisProblemDTO> diagnosisProblem(@RequestBody DiagnosisProblemBody requestBody) throws Exception{
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseEntity<DiagnosisProblemDTO> output;
        
        try {
        	DiagnosisProblemDTO body = new DiagnosisProblemDTO(problemService.getDiagnosisProblem(requestBody.getSubject()));
			
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
	
	@PostMapping(value = "problems/error/report")
	public ResponseEntity<ErrorDTO> errorReport(@RequestBody ErrorReportBody errorReportBody) throws Exception{
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		ResponseEntity<ErrorDTO> output;
		String resultMessage;
		try {
			resultMessage = problemService.insertErrorReport(errorReportBody.getProblem_id(), errorReportBody.getId(), errorReportBody.getReport_type(), errorReportBody.getReport_text());
			output = new ResponseEntity<>(new ErrorDTO(resultMessage),headers, HttpStatus.OK );
		}catch(UnavailableTypeException e) {
			output = new ResponseEntity<>(new ErrorDTO(e.toString()),headers, HttpStatus.NOT_FOUND );
		}catch(Exception e) {
			resultMessage = "failed";
			output = new ResponseEntity<>(new ErrorDTO(resultMessage),headers, HttpStatus.NOT_FOUND );
		}
//		
		return output;
	}
}
