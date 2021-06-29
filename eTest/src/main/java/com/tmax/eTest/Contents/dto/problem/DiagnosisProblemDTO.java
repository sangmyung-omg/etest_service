package com.tmax.eTest.Contents.dto.problem;

import java.util.List;

import lombok.Data;
@Data
public class DiagnosisProblemDTO {
	String message;
	List<Integer> data;
	
	public DiagnosisProblemDTO() {
		
	}
	public DiagnosisProblemDTO(List<Integer> problems) {
		this.message = "success";
		this.data = problems;
	}
}
