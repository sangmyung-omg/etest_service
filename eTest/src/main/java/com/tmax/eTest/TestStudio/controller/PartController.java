package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.problem.Part;
import com.tmax.eTest.Common.repository.problem.PartRepo;
import com.tmax.eTest.TestStudio.dto.PartListDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PartController {

	private final PartRepo partRepo;

	@GetMapping("test-studio/part")
	@ResponseBody
	public ResponseEntity<List<PartListDTO>> PartList() {
		try {
			List<Part> query = partRepo.findAll();
			List<PartListDTO> body = query.stream().map(m -> new PartListDTO(
					m.getPartID(),
					m.getPartName(),
					m.getOrderNum(),
					m.getProblemCount()
					)).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("test-studio/part")
	@ResponseBody
	public ResponseEntity<String> PartPost(@RequestBody @Valid CreatePartRequest request) {
		try {
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Data
	static class CreatePartRequest {
		private String name;
	}
}