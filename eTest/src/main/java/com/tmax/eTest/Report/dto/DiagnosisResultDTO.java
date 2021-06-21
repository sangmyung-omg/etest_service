package com.tmax.eTest.Report.dto;

import java.util.List;

import lombok.Data;
@Data
public class DiagnosisResultDTO {
	String message;
	List<Long> data;
	
	public DiagnosisResultDTO() {
		
	}
}
