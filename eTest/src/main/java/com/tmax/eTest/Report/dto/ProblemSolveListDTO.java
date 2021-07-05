package com.tmax.eTest.Report.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProblemSolveListDTO {
	public List<String> probIdList;
	public List<String> correctList;
}
