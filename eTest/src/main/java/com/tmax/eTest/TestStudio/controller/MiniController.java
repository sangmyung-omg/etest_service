package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.TestStudio.dto.MiniProblemListDTO;
import com.tmax.eTest.TestStudio.repository.MiniRepository;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MiniController {

	private final MiniRepository miniRepository;

	@GetMapping("test-studio/problems/mini")
	public ResponseEntity<List<MiniProblemListDTO>> MiniProblemList(
			@RequestParam(value = "partId", required = false) Integer partId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderOption", defaultValue = "ascending") String orderOption,
			@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
			@RequestParam(value = "pageCount", defaultValue = "20") int pageCount) {
		try {
			return new ResponseEntity<>(miniRepository.searchMiniProblems(partId, keyword, order, orderOption, PageRequest.of(pageNo, pageCount))
					.getContent().stream().map(MiniProblemListDTO::new).collect(Collectors.toList()), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}