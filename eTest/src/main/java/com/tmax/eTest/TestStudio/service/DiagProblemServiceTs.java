package com.tmax.eTest.TestStudio.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;
import com.tmax.eTest.TestStudio.repository.DiagProblemRepositoryTs;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DiagProblemServiceTs {
	
	private final DiagProblemRepositoryTs diagProblemRepository;
	
	/**
	 * 문제 조회
	 */
	//id 로 조회
	
	public DiagnosisProblem findOne(Long probID) {
		
		return diagProblemRepository.findById(probID.intValue()).get();
	}
	
	public List<DiagnosisProblem> findByIdOrderSorted(Long curriculumID) {
		
		Sort sort =sortByOrder();
		
		return diagProblemRepository.findByCurriculumIdIs( curriculumID.intValue(), sort );
	}
	
	
	private Sort sortByOrder() {
		return Sort.by(Sort.Direction.DESC, "orderNum");
	}
}
