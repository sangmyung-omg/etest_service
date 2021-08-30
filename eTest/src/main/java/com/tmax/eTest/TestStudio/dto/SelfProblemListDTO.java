package com.tmax.eTest.TestStudio.dto;

import com.tmax.eTest.Common.model.problem.DiagnosisProblem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelfProblemListDTO {
	private Integer id;
	private Integer order;
	private String difficulty;
	
	public SelfProblemListDTO(DiagnosisProblem d) {
		this.id = d.getProbId();
		this.order = d.getOrderNum();
		this.difficulty = d.getProblem().getDifficulty();
	}
}