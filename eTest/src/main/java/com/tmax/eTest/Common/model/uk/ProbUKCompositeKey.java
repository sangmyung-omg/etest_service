package com.tmax.eTest.Common.model.uk;

import java.io.Serializable;

import com.tmax.eTest.Common.model.problem.Problem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProbUKCompositeKey implements Serializable{
	private long probID;
	private long ukId;
}
