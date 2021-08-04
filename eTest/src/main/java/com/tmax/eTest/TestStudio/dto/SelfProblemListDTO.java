package com.tmax.eTest.TestStudio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SelfProblemListDTO {
	private Integer id;
	private Integer order;
	private String difficulty;
}