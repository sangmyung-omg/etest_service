package com.tmax.eTest.TestStudio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.TestStudio.controller.component.exception.NoDataExceptionTs;
import com.tmax.eTest.TestStudio.repository.DiagProblemRepositoryTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiagProblemServiceTs {
	
	private final DiagProblemRepositoryTs diagProblemRepository;
	
	/**
	 * 문제 조회
	 * @throws Exception 
	 */
	//id 로 조회
	
	public Optional<DiagnosisProblem> findOne(Long probID) throws NoDataExceptionTs {
		
		if( diagProblemRepository.findById(probID.intValue()).isPresent() ) {
			return diagProblemRepository.findById(probID.intValue());
		}else {
			throw new NoDataExceptionTs("DiagnosisProblem", probID.toString());
		}
		
	}
	
	public List<DiagnosisProblem> findByIdOrderSorted(Long curriculumID) {
		
		Sort sort =sortByOrder();
		
		return diagProblemRepository.findByCurriculumIdIs( curriculumID.intValue(), sort );
	}
	
	
	private Sort sortByOrder() {
		return Sort.by(Sort.Direction.DESC, "orderNum");
	}
}
