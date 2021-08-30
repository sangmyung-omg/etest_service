package com.tmax.eTest.TestStudio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmax.eTest.Common.repository.problem.DiagnosisCurriculumRepo;
import com.tmax.eTest.TestStudio.dto.CurriculumListDTO;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CurriculumService {

	private final DiagnosisCurriculumRepo diagnosisCurriculumRepo;
	
	public List<CurriculumListDTO> read() {
		return diagnosisCurriculumRepo.findAll().stream().map(CurriculumListDTO::new).collect(Collectors.toList());
	}
	
	public void update(Integer id, String status) {
		diagnosisCurriculumRepo.getById(id).updateCurriculum(status);
	}
	
}