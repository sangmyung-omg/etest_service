package com.tmax.eTest.TestStudio.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.TestStudio.dto.CurriculumListDTO;
import com.tmax.eTest.TestStudio.service.CurriculumService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CurriculumController {

	private final CurriculumService curriculumService;

	@GetMapping("test-studio/curriculum")
	public ResponseEntity<List<CurriculumListDTO>> CurriculumList() {
		return new ResponseEntity<>(curriculumService.read(), HttpStatus.OK);
	}

	@PatchMapping("test-studio/curriculum/{id}")
	public ResponseEntity<String> CurriculumPost(@PathVariable("id") Integer id, @RequestBody PartRequest request) {
		try {
			curriculumService.update(id, request.getStatus());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (DataAccessException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@Data
	static class PartRequest {
		private String status;
	}
	
}