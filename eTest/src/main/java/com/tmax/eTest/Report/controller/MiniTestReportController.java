package com.tmax.eTest.Report.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmax.eTest.Report.dto.MiniTestResultDTO;
import com.tmax.eTest.Report.dto.PartUnderstandingDTO;

@RestController
public class MiniTestReportController {
	
	@CrossOrigin("*")
	@GetMapping(value="/report/miniTestResult/{id}", produces = "application/json; charset=utf-8")
	public MiniTestResultDTO miniTestResult(@PathVariable("id") String id) throws Exception{
		MiniTestResultDTO output = new MiniTestResultDTO();
		
		output.initForDummy();

		return output;
	}
	
}
