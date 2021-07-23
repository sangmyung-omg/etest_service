package com.tmax.eTest.Common.model.problem;

import java.util.Optional;

import lombok.Data;

@Data
public class TestProblemBody {
	private int subject;
	private Optional<Integer> index = Optional.empty();
}
