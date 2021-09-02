package com.tmax.eTest.Contents.controller.problem; 

import java.nio.charset.Charset;

import com.tmax.eTest.Common.model.error_report.ErrorReportBody;
import com.tmax.eTest.Common.model.problem.DiagnosisProblemBody;
import com.tmax.eTest.Contents.dto.problem.DiagnosisProblemDTO;
import com.tmax.eTest.Contents.dto.problem.ErrorDTO;
import com.tmax.eTest.Contents.dto.problem.ProblemDTO;
import com.tmax.eTest.Contents.exception.problem.NoDataException;
import com.tmax.eTest.Contents.exception.problem.UnavailableTypeException;
import com.tmax.eTest.Contents.service.ProblemServicesBase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/contents" + "/v0")
public class ProblemControllerV0 {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("ProblemServicesV0")
	ProblemServicesBase problemService;	
	
	@GetMapping(value="/problems/{id}")
	public ResponseEntity<ProblemDTO> problem(@PathVariable("id") Integer id) throws Exception{
		logger.info("> problem info logic start! : " + Integer.toString(id));
		HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ResponseEntity<ProblemDTO> output;

		try {
			ProblemDTO body = problemService.getProblem(id);

			body.setMessage("success");
			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
		} catch (NoDataException e) {
			ProblemDTO body = new ProblemDTO();
			body.setMessage("Failed: " + e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ProblemDTO body = new ProblemDTO();
			body.setMessage("Failed: " + e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		}
		return output;
	}

	// @GetMapping(value="/problems/test")
	// public ResponseEntity<TestProblemDTO> testProblem(@RequestBody
	// TestProblemBody requestBody) throws Exception{
	// HttpHeaders headers= new HttpHeaders();
	// headers.setContentType(new MediaType("application", "json",
	// Charset.forName("UTF-8")));
	// ResponseEntity<TestProblemDTO> output;
	// Integer newIndex = requestBody.getIndex().orElseGet(()->0);
	//
	// try {
	// TestProblemDTO body = new
	// TestProblemDTO(problemService.getTestProblem(requestBody.getSubject(),
	// newIndex));
	//
	// output = new ResponseEntity<>(body, headers, HttpStatus.OK);
	// }catch(NoDataException e) {
	// TestProblemDTO body = new TestProblemDTO();
	// body.setMessage("Failed: "+e.getMessage());
	// output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
	// }catch(Exception e) {
	// TestProblemDTO body = new TestProblemDTO();
	// body.setMessage("Failed: "+e.getMessage());
	// output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
	// }
	//
	// return output;
	// }

	@GetMapping(value = "/problems/diagnosis")
	public ResponseEntity<DiagnosisProblemDTO> diagnosisProblem(@RequestBody DiagnosisProblemBody requestBody)
			throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		ResponseEntity<DiagnosisProblemDTO> output;

		try {
			DiagnosisProblemDTO body = new DiagnosisProblemDTO(problemService.getDiagnosisProblem(requestBody.getSubject()));

			output = new ResponseEntity<>(body, headers, HttpStatus.OK);
		} catch (NoDataException e) {
			DiagnosisProblemDTO body = new DiagnosisProblemDTO();
			body.setMessage("Failed: " + e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			DiagnosisProblemDTO body = new DiagnosisProblemDTO();
			body.setMessage("Failed: " + e.getMessage());
			output = new ResponseEntity<>(body, headers, HttpStatus.NOT_FOUND);
		}

		return output;
	}

	@PostMapping(value = "problems/error/report")
	public ResponseEntity<ErrorDTO> errorReport(@RequestBody ErrorReportBody errorReportBody) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		ResponseEntity<ErrorDTO> output;
		String resultMessage;
		try {
			resultMessage = problemService.insertErrorReport(errorReportBody.getProblem_id(), errorReportBody.getId(),
					errorReportBody.getReport_type(), errorReportBody.getReport_text());
			output = new ResponseEntity<>(new ErrorDTO(resultMessage), headers, HttpStatus.OK);
		} catch (UnavailableTypeException e) {
			output = new ResponseEntity<>(new ErrorDTO(e.toString()), headers, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			resultMessage = "failed";
			output = new ResponseEntity<>(new ErrorDTO(resultMessage), headers, HttpStatus.NOT_FOUND);
		}
		//
		return output;
	}
}
