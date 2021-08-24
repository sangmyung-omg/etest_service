package com.tmax.eTest.Common.model.problem;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProblemChoiceCompositeKey implements Serializable {
	private long probID;
	private long choiceNum;
}
