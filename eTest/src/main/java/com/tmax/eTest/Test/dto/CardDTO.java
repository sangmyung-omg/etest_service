package com.tmax.eTest.Test.dto;

import java.util.List;

import lombok.Data;

@Data
public class CardDTO {
	private final String cardType;
	private final String cardTitle;
	private final List<ProblemDTO> problemIdList;
}
