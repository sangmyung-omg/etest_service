package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemServiceETest {
	
	private final ProblemRepositoryETest problemRepository;
	private final PathUtilEtest pathUtilEtest = new PathUtilEtest();
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public Problem findOne(Long probId) {
		return problemRepository.findById( probId.intValue() ).get();
	}
	
	/**
	 * 문제 생성
	 */
	public Problem problemCreate(Problem problem) {
		return problemRepository.save( problem );
	}
	
	
	/**
	 * 문제 업데이트 
	 */
	
	public void problemValidate(String userId, String probId ) {
		Problem problem = problemRepository.findById( Integer.parseInt( probId ) ).get();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		problem.setValiatorID(userId);
		problem.setValiateDate(timestamp);
	}
	
	public String problemUpdate(String userId, BaseProblemDTO requestInfo ) {
		//id not null 일경우면 update
		if(requestInfo.getProbID() ==null) return null;
		
		Problem problem = problemRepository.findById( Integer.parseInt( requestInfo.getProbID() ) ).get();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		
		if(requestInfo.getAnswerType()!=null)
			problem.setAnswerType(requestInfo.getAnswerType());
		
		if(requestInfo.getQuestion()!=null)
			problem.setQuestion(requestInfo.getQuestion());
		
		if(requestInfo.getSolution()!=null)
			problem.setSolution(requestInfo.getSolution());
		
		if(requestInfo.getDifficulty()!=null)
			problem.setDifficulty(requestInfo.getDifficulty());
		
		if(requestInfo.getCategory()!=null)
			problem.setCategory(requestInfo.getCategory());
		
//		if(requestInfo.getImgSrc()!=null)
//			problem.setImgSrc(requestInfo.getImgSrc());
		if(!requestInfo.getImgListIn().isEmpty()) {
			problem.setImgSrc(pathUtilEtest.getDirPath() + File.separator + problem.getProbID());
		}
		
		if(requestInfo.getTimeRecommendation()!=null)
			problem.setTimeReco(requestInfo.getTimeRecommendation());
		
		if(requestInfo.getSource()!=null)
			problem.setSource(requestInfo.getSource());
		
		if(requestInfo.getIntention()!=null)
			problem.setIntention(requestInfo.getIntention());
		
		if(requestInfo.getQuestionInitial()!=null)
			problem.setQuestionInitial(requestInfo.getQuestionInitial());
		
		if(requestInfo.getSolutionInitial()!=null)
			problem.setSolutionInitial(requestInfo.getSolutionInitial());

		if( "T".equals( requestInfo.getValidatorID() ) ) {
			problem.setValiatorID(userId);
			problem.setValiateDate(timestamp);
		}
		
		problem.setEditorID(userId);
		problem.setEditDate(timestamp);
		
		return "ok";
	}
	
	
	/**
	 *  삭제
	 */
	public String problemDeleteById(Long problemId) {
		problemRepository.deleteById( problemId.intValue() ); 
		return "ok";
	}

}
