package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbUKRelQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProbUKRelRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryTs;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryTs;
import com.tmax.eTest.TestStudio.repository.UKRepositoryTs;
import com.tmax.eTest.TestStudio.util.PathUtilTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UKServiceTs {

	private final UKRepositoryTs ukRepositoryETest;

	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public Optional<UkMaster> findOneByUKId(Long ukId) {
		return ukRepositoryETest.findById( ukId.intValue() );
	}
	

}
