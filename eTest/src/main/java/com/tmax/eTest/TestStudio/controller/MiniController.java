package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.problem.TestProblem;
import com.tmax.eTest.TestStudio.dto.MiniProblemListDTO;
import com.tmax.eTest.TestStudio.repository.MiniRepository;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MiniController {

	private final MiniRepository miniRepository;

	@GetMapping("test-studio/problems/mini")
	@ResponseBody
	public ResponseEntity<List<MiniProblemListDTO>> MiniProblemList(
			@RequestParam(value="partId", required=false) Integer partId,
			@RequestParam(value="keyword", required=false) String keyword,
			@RequestParam(value="order", required=false) String order,
			@RequestParam(value="orderOption",defaultValue="ascending") String orderOption,
			@RequestParam(value="pageNo", defaultValue="0") int pageNo,
			@RequestParam(value="pageCount",defaultValue="20") int pageCount) {
		try {
			Page<TestProblem> query = miniRepository.searchMiniProblems(partId, keyword, order, orderOption, PageRequest.of(pageNo, pageCount));
			List<MiniProblemListDTO> body = query.getContent().stream().map(m -> new MiniProblemListDTO(
					m.getProbID(),
					ObjectUtils.isEmpty(m.getPart()) ? null : m.getPart().getPartName(),
					m.getProblem().getDifficulty(),
					m.getSubject(),
					m.getStatus(),
					m.getProblem().getQuestion(),
					m.getProblem().getEditDate()
					)).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

}