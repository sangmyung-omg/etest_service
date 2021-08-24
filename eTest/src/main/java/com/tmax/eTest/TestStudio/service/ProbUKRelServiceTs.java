package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbUKRelQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbUKRelRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProbUKRelServiceTs {

	private final ProbUKRelRepositoryTs probUKRelRepositoryETest;
	private final ProbUKRelQRepositoryTs probUKRelQRepositoryETest;

	
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
	public List<ProblemUKRelation> probUKRelCreateAll(Set<ProblemUKRelation> problemUKRelations) {
		return probUKRelRepositoryETest.saveAll( problemUKRelations );
	}
	
	/**
	 *  삭제
	 */
	public String probUKRelDeleteAllByProbId(Long problemId) {
		probUKRelQRepositoryETest.probUKRelDeleteAllByProbId( problemId.intValue() ); 
		return "ok";
	}

}
