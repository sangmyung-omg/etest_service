package com.tmax.eTest.TestStudio.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.TestStudio.dto.MiniProblemListDTO;
import com.tmax.eTest.TestStudio.dto.MiniProblemListDTOContents;
import com.tmax.eTest.TestStudio.repository.MiniRepository;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MiniController {

	private final MiniRepository miniRepository;

	@GetMapping("test-studio/problems/mini")
	public ResponseEntity<MiniProblemListDTO> MiniProblemList(
			@RequestParam(value = "partId", required = false) Integer partId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderOption", defaultValue = "ascending") String orderOption,
			@RequestParam(value = "skip", defaultValue = "0") int skip,
			@RequestParam(value = "limit", defaultValue = "20") int limit) {
//			return new ResponseEntity<>(miniRepository.searchMiniProblems(partId, keyword, order, orderOption, PageRequest.of(pageNo, pageCount))
//					.getContent().stream().map(MiniProblemListDTO::new).collect(Collectors.toList()), HttpStatus.OK);
		Page<MiniProblemListDTOContents> result = miniRepository.searchMiniProblems(partId, keyword, order, orderOption, PageRequest.of(skip, limit));
		return new ResponseEntity<>(new MiniProblemListDTO((int)result.getTotalElements(), result.getTotalPages(), result.getContent()), HttpStatus.OK);
	}

}