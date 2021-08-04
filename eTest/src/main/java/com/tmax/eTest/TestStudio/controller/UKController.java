package com.tmax.eTest.TestStudio.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmax.eTest.Common.model.uk.UkMaster;
import com.tmax.eTest.Common.repository.uk.UkMasterRepo;
import com.tmax.eTest.TestStudio.dto.UKListDTO;

import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UKController {

	private final UkMasterRepo ukMasterRepo;

	@GetMapping("test-studio/uk")
	@ResponseBody
	public ResponseEntity<List<UKListDTO>> UKList() {
		try {
			List<UkMaster> query = ukMasterRepo.findAll();
			List<UKListDTO> body = query.stream().map(m -> new UKListDTO(m.getUkId(),m.getUkName())).collect(Collectors.toList());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}