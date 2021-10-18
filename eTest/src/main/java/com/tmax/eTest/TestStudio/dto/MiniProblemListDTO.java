package com.tmax.eTest.TestStudio.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiniProblemListDTO {
	private Integer totalCount;
	private Integer totalPage;
	private List<MiniProblemListDTOContents> contents;
}