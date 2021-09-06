package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryTs;
import com.tmax.eTest.TestStudio.util.InitialConsonantTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemServiceTs {
	
	private final ProblemRepositoryTs problemRepository;
	private final ProblemQRepositoryTs problemQRepositoryETest;
	private final PathUtilTs pathUtilEtest = new PathUtilTs();
	private final InitialConsonantTs initialConsonantTs;
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public Optional<Problem> findOne(Long probId) {
//		return problemRepository.findById( probId.intValue() ).get();
		return problemRepository.findById( probId.intValue() );
	}
	
	public Problem findOneSet(Long probId) {
		return problemRepository.findPANDPCByProbID( probId.intValue() );
	}
	
	
	/**
	 * 문제 생성
	 */
	public Problem problemCreate(Problem problem) throws Exception {
		return problemRepository.save( problem );
		
	}
	
	
	/**
	 * 문제 업데이트 
	 * @throws Exception 
	 */
	
	public void problemValidate(String userId, String probId ) throws Exception {
		
		if( problemRepository.findById( Integer.parseInt( probId ) ).isPresent() ) {
			
			Problem problem = problemRepository.findById( Integer.parseInt( probId ) ).get();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			problem.setValiatorID(userId);
			problem.setValiateDate(timestamp);
			
		}else {
			throw new NoDataExceptionTs("Problem", probId);
		}

	}
	
	public String problemUpdate(String userId, BaseProblemDTO requestInfo ) throws Exception {
		//id not null 일경우면 update
		if(requestInfo.getProbID() ==null) return null;
		
		if( problemRepository.findById( Integer.parseInt( requestInfo.getProbID() ) ).isPresent() ) {
			
			Problem problem = problemRepository.findById( Integer.parseInt( requestInfo.getProbID() ) ).get();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			
			if(requestInfo.getAnswerType()!=null)
				problem.setAnswerType(requestInfo.getAnswerType());
			
			if(requestInfo.getQuestion()!=null) {
				problem.setQuestion(requestInfo.getQuestion());
				problem.setQuestionInitial( initialConsonantTs.InitialConsonantsV2( requestInfo.getQuestion() ) );
			}
			
			if(requestInfo.getSolution()!=null) {
				problem.setSolution(requestInfo.getSolution());
				problem.setSolutionInitial( initialConsonantTs.InitialConsonantsV2( requestInfo.getSolution() ) );
			}
				
			if(requestInfo.getDifficulty()!=null)
				problem.setDifficulty(requestInfo.getDifficulty());
			
			if(requestInfo.getCategory()!=null)
				problem.setCategory(requestInfo.getCategory());
			
//			if(requestInfo.getImgSrc()!=null)
//				problem.setImgSrc(requestInfo.getImgSrc());
			
			if(requestInfo.getImgSrcListIn()!=null) {
				if(!requestInfo.getImgSrcListIn().isEmpty()) {
					problem.setImgSrc(pathUtilEtest.getDirPath() + File.separator + problem.getProbID());
				}
			}
			
			if(requestInfo.getTimeRecommendation()!=null)
				problem.setTimeReco( Long.parseLong(requestInfo.getTimeRecommendation()) );
			
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
			
		}else {
			throw new NoDataExceptionTs("Problem", requestInfo.getProbID());
		}
		
		return "ok";
	}
	
	
	/**
	 *  삭제
	 */
	public String problemDeleteById(Long problemId) throws Exception {
		problemRepository.deleteById( problemId.intValue() ); 
		return "ok";
	}
	
	////
	public Integer NPID() {
	
		if(problemQRepositoryETest.searchLatestProblem().getProbID() == null ) {
			return 1;
		}else {
			return problemQRepositoryETest.searchLatestProblem().getProbID() + 1;
		}
	
	}
}
