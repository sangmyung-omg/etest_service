package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.ProblemChoiceCompositeKey;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProbChoiceServiceETest {
	
	private final ProbChoiceRepositoryETest probChoiceRepositoryETest;
	private final ProbChoiceQRepositoryETest probChoiceQRepositoryETest;
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public List<ProblemChoice> findAllByProbId(Long probId) {
		return probChoiceRepositoryETest.findByProbIDOnlyIs( probId.intValue() );
	}
	
	/**
	 * 문제 생성
	 */
	public ProblemChoice probChoiceCreate(ProblemChoice problemChoice) {
		return probChoiceRepositoryETest.save( problemChoice );
	}
	
	
	/**
	 * 문제 업데이트 
	 */
	
	public String probChoiceUpdate(String userId, List<BaseProbChoiceDTO> requestInfos, Long LongProbId ) {
		//id not null 일경우면 update
		if(requestInfos==null) return null;
		
		for(BaseProbChoiceDTO requestInfo : requestInfos) {
			if(requestInfo.getChoiceNum() == null) continue ;
			
			ProblemChoiceCompositeKey compositeKey = new ProblemChoiceCompositeKey();
			compositeKey.setProbID( LongProbId );
			compositeKey.setChoiceNum( Integer.parseInt( requestInfo.getChoiceNum() ) );
			ProblemChoice problemChoice = probChoiceRepositoryETest.findById( compositeKey ).get();
			
//			if(requestInfo.getChoiceNum()!=null)
//				problemChoice.setChoiceNum( Integer.parseInt( requestInfo.getChoiceNum() ) );
			
			if(requestInfo.getUkID()!=null) {
				UkMaster temp = new UkMaster();
				temp.setUkId( Integer.parseInt( requestInfo.getUkID() ) );
				problemChoice.setUkId( temp );
//				problemChoice.setUkIDOnly( Integer.parseInt( requestInfo.getUkID() ) );
			}
			if(requestInfo.getChoiceScore()!=null)
				problemChoice.setChoiceScore( Integer.parseInt( requestInfo.getChoiceScore() ) );
		}
		return "ok";
	}
	
	
	/**
	 *  삭제
	 */
	public String probChoiceDeleteAllByProbId(Long problemId) {
		probChoiceQRepositoryETest.probChoiceDeleteByProbId( problemId.intValue() ); 
		return "ok";
	}
	
}
