package com.tmax.eTest.TestStudio.service;



import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseTestProblemDTO;
import com.tmax.eTest.TestStudio.repository.TestProblemRepositoryETest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TestProblemServiceETest {
	
	private final TestProblemRepositoryETest testProblemRepositoryETest;
	
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
	
	public TestProblem findOne(Long probID) {
		
		return testProblemRepositoryETest.findById(probID.intValue()).get();
	}
	
	
	/**
	 * 문제 업데이트 
	 */
	
	public String testProblemUpdate(String userId, BaseTestProblemDTO requestInfo ) {
		  
		//id not null 일경우면 update
		if(requestInfo.getProbID() ==null) return null;
				
		TestProblem testProblem = testProblemRepositoryETest.findById( Integer.parseInt( requestInfo.getProbID() ) ).get();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		
		if(requestInfo.getPartID()!=null)
			testProblem.setPartID( Integer.parseInt( requestInfo.getPartID() ) );
		
		if(requestInfo.getSubject()!=null)
			testProblem.setSubject(requestInfo.getSubject());
		
		if(requestInfo.getStatus()!=null)
			if("출제".equals(requestInfo.getStatus()) || "보류".equals(requestInfo.getStatus())) {
				testProblem.setStatus(requestInfo.getStatus());
			}
		
		return "ok";
	}
	
	public String testProbStatusChange(String probID ) {
		  
		//id not null 일경우면 update
		if(probID == null) return null;
		TestProblem testProblem = testProblemRepositoryETest.findById( Integer.parseInt( probID ) ).get();
		
		if( "출제".equals( testProblem.getStatus() ) ) {
			testProblem.setStatus("보류");
		}else if( "보류".equals( testProblem.getStatus() ) ) {
			testProblem.setStatus("출제");
		}else {
			return "fail";
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
