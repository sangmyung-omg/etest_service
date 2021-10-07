package com.tmax.eTest.TestStudio.service;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseDiagCurriculumDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.DiagCurriculumRepositoryTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiagCurriculumServiceTs {
	
	private final DiagCurriculumRepositoryTs diagCurriculumRepository;
	private final ProblemServiceTs problemServiceETest;
	private final DiagProblemServiceTs diagProblemServiceETest;
	private final PathUtilTs pathUtilEtest;
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public Optional<DiagnosisCurriculum>  findOne(Long curriculcumId) {
		return diagCurriculumRepository.findById( curriculcumId.intValue() );
	}
	
	/**
	 * 문제 업데이트 
	 * @throws Exception 
	 */
	
	public String problemUpdate(String userId, BaseDiagCurriculumDTO requestInfo ) throws NoDataExceptionTs {
		  
		//id not null 일경우면 update
		if(requestInfo.getCurriculumID() ==null) return null;
		
		DiagnosisCurriculum diagnosisCurriculum = new DiagnosisCurriculum();
		
		if( diagCurriculumRepository.findById( Integer.parseInt( requestInfo.getCurriculumID() ) ).isPresent() ) {
			diagnosisCurriculum = diagCurriculumRepository.findById( Integer.parseInt( requestInfo.getCurriculumID() ) ).get();
		}else {
			throw new NoDataExceptionTs( "DiagnosisCurriculum", requestInfo.getCurriculumID().toString() );
		}
		
		if(requestInfo.getSubject()!=null)
			diagnosisCurriculum.setSubject(requestInfo.getSubject());
		
//		if(requestInfo.getStatus()!=null)
//			if(pathUtilEtest.getStatusOn().equals(requestInfo.getStatus()) || pathUtilEtest.getStatusOff().equals(requestInfo.getStatus())) {
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
	
	public String currStatusChange(String curriculumId ) throws NoDataExceptionTs {
		  
		//id not null 일경우면 update
		if(curriculumId == null) return null;
		
		DiagnosisCurriculum diagnosisCurriculum = new DiagnosisCurriculum();
		
		if( diagCurriculumRepository.findById( Integer.parseInt( curriculumId ) ).isPresent() ) {
			diagnosisCurriculum = diagCurriculumRepository.findById( Integer.parseInt( curriculumId ) ).get();
		}else {
			throw new NoDataExceptionTs("DiagnosisCurriculum", curriculumId.toString());
		}
		
		if( pathUtilEtest.getStatusOn().equals( diagnosisCurriculum.getStatus() ) ) {
			diagnosisCurriculum.setStatus(pathUtilEtest.getStatusOff());
		}else if( pathUtilEtest.getStatusOff().equals( diagnosisCurriculum.getStatus() ) ) {
			diagnosisCurriculum.setStatus(pathUtilEtest.getStatusOn());
		}else {
			return "fail";
		}
		
		return "ok";
	}
}
