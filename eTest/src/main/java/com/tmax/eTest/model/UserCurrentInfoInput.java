package com.tmax.eTest.model;

import lombok.Data;

@Data
public class UserCurrentInfoInput {
	private String userId;
	private String grade;
	private String semester;
	private String chapter;
}
