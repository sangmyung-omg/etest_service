package com.tmax.eTest.TestStudio.service;



import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemDTO;
import com.tmax.eTest.TestStudio.repository.TestProblemRepositoryTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TestProblemServiceTs {
	
	private final TestProblemRepositoryTs testProblemRepositoryETest;
	private final PathUtilTs pathUtilEtest;
	
	/**
	 *  생성
	 */
	public TestProblem testProblemCreate(TestProblem testProblem) {
		return testProblemRepositoryETest.save( testProblem );
	}
	
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	
	public Optional<TestProblem> findOne(Long probID) {
		
		return testProblemRepositoryETest.findById(probID.intValue());
	}
	
	
	/**
	 * 문제 업데이트 
	 * @throws Exception 
	 */
	
	public String testProblemUpdate(String userId, BaseTestProblemDTO requestInfo ) throws Exception {
		  
		//id not null 일경우면 update
		if(requestInfo.getProbID() ==null) return null;
				
		if(testProblemRepositoryETest.findById( Integer.parseInt( requestInfo.getProbID() ) ).isPresent()) {
			
			TestProblem testProblem = testProblemRepositoryETest.findById( Integer.parseInt( requestInfo.getProbID() ) ).get();
			
			if(requestInfo.getPartID()!=null)
				testProblem.setPartID( Integer.parseInt( requestInfo.getPartID() ) );
			
			if(requestInfo.getSubject()!=null)
				testProblem.setSubject(requestInfo.getSubject());
			
//			if(requestInfo.getStatus()!=null)
//				if(pathUtilEtest.getStatusOn().equals(requestInfo.getStatus()) || pathUtilEtest.getStatusOff().equals(requestInfo.getStatus())) {
//					testProblem.setStatus(requestInfo.getStatus());
//				}
			
		}else {
			throw new NoDataExceptionTs("TestProblem",requestInfo.getProbID());
		}
		
		return "ok";
	}
	
	public String testProbStatusChange(String probID ) throws Exception {
		  
		//id not null 일경우면 update
		if(probID == null) return null;
		
		if(testProblemRepositoryETest.findById( Integer.parseInt( probID ) ).isPresent()) {
			
			TestProblem testProblem = testProblemRepositoryETest.findById( Integer.parseInt( probID ) ).get();
			
			if( pathUtilEtest.getStatusOn().equals( testProblem.getStatus() ) ) {
				testProblem.setStatus(pathUtilEtest.getStatusOff());
			}else if( pathUtilEtest.getStatusOff().equals( testProblem.getStatus() ) ) {
				testProblem.setStatus(pathUtilEtest.getStatusOn());
			}else {
				return "fail";
			}
			
		}else {
			throw new NoDataExceptionTs("TestProblem",probID);
		}
	
		return "ok";
	}
	
	
	/**
	 *  삭제
	 */
	public String testProblemDeleteById(Long testProblemId) {
		testProblemRepositoryETest.deleteById( testProblemId.intValue() ); 
		return "ok";
	}
	
	
}
