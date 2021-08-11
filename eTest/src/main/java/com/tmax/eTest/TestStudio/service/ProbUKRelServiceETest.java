package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbUKRelQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbUKRelRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProbUKRelServiceETest {

	private final ProbUKRelRepositoryETest probUKRelRepositoryETest;
	private final ProbUKRelQRepositoryETest probUKRelQRepositoryETest;

	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public List<ProblemUKRelation> findAllByProbId(Long probId) {
		return probUKRelRepositoryETest.findAllByProbID_ProbIDIs( probId.intValue() );
	}
	
	public List<ProblemUKRelation> findAllWUKByProbId(Long probId) {
		return probUKRelRepositoryETest.findUKRANDUKByProbID_ProbIDIs( probId.intValue() );
	}
	
	/**
	 * 문제 생성
	 */
	public ProblemUKRelation probUKRelCreate(ProblemUKRelation problemUKRelation) {
		return probUKRelRepositoryETest.save( problemUKRelation );
	}
	
	
	/**
	 *  삭제
	 */
	public String probUKRelDeleteAllByProbId(Long problemId) {
		probUKRelQRepositoryETest.probUKRelDeleteAllByProbId( problemId.intValue() ); 
		return "ok";
	}

}
