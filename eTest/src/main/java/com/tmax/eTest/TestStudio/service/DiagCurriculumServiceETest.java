package com.tmax.eTest.TestStudio.service;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.DiagCurriculumRepositoryETest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiagCurriculumServiceETest {
	
	private final DiagCurriculumRepositoryETest diagCurriculumRepository;
	
	private final ProblemServiceETest problemServiceETest;
	private final DiagProblemServiceETest diagProblemServiceETest;
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public DiagnosisCurriculum findOne(Long curriculcumId) {
		return diagCurriculumRepository.findById( curriculcumId.intValue() ).get();
	}
	
	/**
	 * 문제 업데이트 
	 */
	
	public String problemUpdate(String userId, BaseDiagCurriculumDTO requestInfo ) {
		  
		//id not null 일경우면 update
		if(requestInfo.getCurriculumID() ==null) return null;
		DiagnosisCurriculum diagnosisCurriculum = diagCurriculumRepository.findById( Integer.parseInt( requestInfo.getCurriculumID() ) ).get();
		
		if(requestInfo.getSubject()!=null)
			diagnosisCurriculum.setSubject(requestInfo.getSubject());
		
//		if(requestInfo.getStatus()!=null)
//			if("출제".equals(requestInfo.getStatus()) || "보류".equals(requestInfo.getStatus())) {
//				diagnosisCurriculum.setStatus(requestInfo.getStatus());
//				
//				for( DiagnosisProblem diagnosisProblem 
//						: diagProblemServiceETest.findByIdOrderSorted( Long.parseLong( requestInfo.getCurriculumID() ) )
//					) {
//					
//					problemServiceETest.problemValidate(userId, diagnosisProblem.getProbId().toString());
//				}
//			}
		return "ok";
	}
	
	public String currStatusChange(String curriculumId ) {
		  
		//id not null 일경우면 update
		if(curriculumId == null) return null;
		DiagnosisCurriculum diagnosisCurriculum = diagCurriculumRepository.findById( Integer.parseInt( curriculumId ) ).get();
		
		if( "출제".equals( diagnosisCurriculum.getStatus() ) ) {
			diagnosisCurriculum.setSubject("보류");
		}else if( "보류".equals( diagnosisCurriculum.getStatus() ) ) {
			diagnosisCurriculum.setSubject("출제");
		}else {
			return "fail";
		}
		
		return "ok";
	}
}
