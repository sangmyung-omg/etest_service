package com.tmax.eTest.Contents.dto.problem;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ProblemDTO {

	private String message;
	private Map<String, Object> data = new HashMap<String, Object>();
	public ProblemDTO(String type, String question, String passage, String preface, String difficulty, Map<Long, String> choices) {
		data.put("difficulty", difficulty);
		data.put("question", question);
		data.put("preface", preface);
		data.put("passage", passage);
		data.put("type", type);
		data.put("choices", choices);
	}
	public ProblemDTO() {
		
	}
}
