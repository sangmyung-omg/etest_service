package com.tmax.eTest.Contents.dto.problem;

import java.util.List;
import lombok.Data;

@Data
public class TestProblemDTO {
	String message;
	List<Integer> data;
	
	public TestProblemDTO() {
		
	}
	public TestProblemDTO(List<Integer> problems) {
		this.message = "success";
		this.data = problems;	
	}
}
