package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.problem.DiagnosisCurriculum;
import com.tmax.eTest.Common.repository.problem.DiagnosisCurriculumRepo;
import com.tmax.eTest.TestStudio.dto.CurriculumListDTO;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CurriculumController {

	private final DiagnosisCurriculumRepo diagnosisCurriculumRepo;

	@GetMapping("test-studio/curriculum")
	@ResponseBody
	public ResponseEntity<List<CurriculumListDTO>> CurriculumList() {
		try {
			List<DiagnosisCurriculum> query = diagnosisCurriculumRepo.findAll();
			List<CurriculumListDTO> body = query.stream().map(m -> new CurriculumListDTO(
					m.getCurriculumId(),
					m.getChapter(),
					m.getSection(),
					m.getSubSection(),
					m.getSetType(),
					m.getSubject(),
					m.getStatus()
					)).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}