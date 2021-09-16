package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.problem.ProblemChoiceCompositeKey;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProbChoiceServiceTs {
	
	private final ProbChoiceRepositoryTs probChoiceRepositoryETest;
	private final ProbChoiceQRepositoryTs probChoiceQRepositoryETest;
	
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
	
	public List<ProblemChoice> probChoiceCreateAll(Set<ProblemChoice> problemChoices) {
		return probChoiceRepositoryETest.saveAll( problemChoices );
	}
	
	
	/**
	 * 문제 업데이트 
	 * @throws Exception 
	 */
	
	public String probChoiceUpdate(String userId, List<BaseProbChoiceDTO> requestInfos, Long LongProbId ) throws Exception {
		//id not null 일경우면 update
		if(requestInfos==null) return null;
		
		for(BaseProbChoiceDTO requestInfo : requestInfos) {
			if(requestInfo.getChoiceNum() == null) continue ;
			
			ProblemChoiceCompositeKey compositeKey = new ProblemChoiceCompositeKey();
			compositeKey.setProbID( LongProbId );
			compositeKey.setChoiceNum( Integer.parseInt( requestInfo.getChoiceNum() ) );
			ProblemChoice problemChoice = new ProblemChoice();
			if(probChoiceRepositoryETest.findById( compositeKey ).isPresent()) {
				problemChoice = probChoiceRepositoryETest.findById( compositeKey ).get();
			}else {
//				throw new NoDataExceptionTs("ProblemChoice","("+LongProbId.toString()+","+requestInfo.getChoiceNum()+")");
				continue;
			}
//			if(requestInfo.getChoiceNum()!=null)
//				problemChoice.setChoiceNum( Integer.parseInt( requestInfo.getChoiceNum() ) );
			
			UkMaster temp = new UkMaster();
			if(requestInfo.getUkID()!=null) {
				temp.setUkId( Integer.parseInt( requestInfo.getUkID() ) );
				problemChoice.setUkId( temp );
//				problemChoice.setUkIDOnly( Integer.parseInt( requestInfo.getUkID() ) );
			}else {
				problemChoice.setUkId( null );
			}
			
			if(requestInfo.getChoiceScore()!=null) {
				problemChoice.setChoiceScore( Integer.parseInt( requestInfo.getChoiceScore() ) );
			}else {
				problemChoice.setChoiceScore( null );
			}
		}
		return "ok";
	}
	
	public String probChoiceUpdate_single(String userId, BaseProbChoiceDTO requestInfo, Long LongProbId ) throws Exception {
		//id not null 일경우면 update
		if(requestInfo==null) return null;
	
		if(requestInfo.getChoiceNum() == null) return null;
		
		ProblemChoiceCompositeKey compositeKey = new ProblemChoiceCompositeKey();
		compositeKey.setProbID( LongProbId );
		compositeKey.setChoiceNum( Integer.parseInt( requestInfo.getChoiceNum() ) );
		ProblemChoice problemChoice = new ProblemChoice();
		if(probChoiceRepositoryETest.findById( compositeKey ).isPresent()) {
			problemChoice = probChoiceRepositoryETest.findById( compositeKey ).get();
		}else {
//			throw new NoDataExceptionTs("ProblemChoice","("+LongProbId.toString()+","+requestInfo.getChoiceNum()+")");
			return null;
		}
		
		UkMaster temp = new UkMaster();
		if(requestInfo.getUkID()!=null) {
			temp.setUkId( Integer.parseInt( requestInfo.getUkID() ) );
			problemChoice.setUkId( temp );
//			problemChoice.setUkIDOnly( Integer.parseInt( requestInfo.getUkID() ) );
		}else {
			problemChoice.setUkId( null );
		}
		
		if(requestInfo.getChoiceScore()!=null) {
			problemChoice.setChoiceScore( Integer.parseInt( requestInfo.getChoiceScore() ) );
		}else {
			problemChoice.setChoiceScore( null );
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
	
	public String probChoiceDeleteAllByProbIdAndChoiceNum(Long problemId, List<Long> choiceNumList) {
		probChoiceQRepositoryETest.probChoiceDeleteByProbIdAndChoiceNum( problemId.intValue(), choiceNumList ); 
		return "ok";
	}
	
}
