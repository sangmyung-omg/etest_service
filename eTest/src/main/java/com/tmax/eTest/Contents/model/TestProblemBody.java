package com.tmax.eTest.Contents.model;

import java.util.Optional;

import lombok.Data;

@Data
public class TestProblemBody {
	private int subject;
	private Optional<Integer> index = Optional.empty();
}
