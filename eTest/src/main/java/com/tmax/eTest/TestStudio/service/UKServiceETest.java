package com.tmax.eTest.TestStudio.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.Problem;
import com.tmax.eTest.Common.model.problem.ProblemChoice;
import com.tmax.eTest.Common.model.uk.ProblemUKRelation;
import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbChoiceDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProbUKRelDTO;
import com.tmax.eTest.TestStudio.dto.problems.base.BaseProblemDTO;
import com.tmax.eTest.TestStudio.repository.ProbChoiceQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbChoiceRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbUKRelQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProbUKRelRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemQRepositoryETest;
import com.tmax.eTest.TestStudio.repository.ProblemRepositoryETest;
import com.tmax.eTest.TestStudio.repository.UKRepositoryETest;
import com.tmax.eTest.TestStudio.util.PathUtilEtest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UKServiceETest {

	private final UKRepositoryETest ukRepositoryETest;

	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	public UkMaster findOneByUKId(Long ukId) {
		return ukRepositoryETest.findById( ukId.intValue() ).get();
	}
	

}
