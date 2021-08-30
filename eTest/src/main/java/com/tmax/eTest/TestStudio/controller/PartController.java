package com.tmax.eTest.TestStudio.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.TestStudio.dto.PartListDTO;
import com.tmax.eTest.TestStudio.service.PartService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PartController {

	private final PartService partService;

	@GetMapping("test-studio/part")
	public ResponseEntity<List<PartListDTO>> PartList() {
		try {
			return new ResponseEntity<>(partService.read(), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("test-studio/part")
	public ResponseEntity<String> PartPost(@RequestBody @Valid PartRequest request) {
		try {
			partService.create(request.name, request.order, request.count);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("test-studio/part/{id}")
	public ResponseEntity<String> PartPost(@PathVariable("id") Integer id, @RequestBody @Valid PartRequest request) {
		try {
			partService.update(id, request.getName(), request.getOrder(), request.getCount());
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("test-studio/part/{id}")
	public ResponseEntity<String> PartPost(@PathVariable("id") Integer id) {
		try {
			partService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Data
	static class PartRequest {
		private String name;
		private Integer order;
		private Integer count;
	}

}