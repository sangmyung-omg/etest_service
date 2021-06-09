package com.tmax.eTest.Contents.dto.problem;

import java.util.List;
import lombok.Data;

@Data
public class TestProblemDTO {
	String message;
	List<Long> data;
	
	public TestProblemDTO() {
		
	}
	public TestProblemDTO(List<Long> problems) {
		this.message = "success";
		this.data = problems;	
	}
}
