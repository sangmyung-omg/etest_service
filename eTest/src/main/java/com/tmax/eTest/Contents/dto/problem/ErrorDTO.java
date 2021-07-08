package com.tmax.eTest.Contents.dto.problem;

import lombok.Data;

@Data
public class ErrorDTO {
	String message = "failed";
	public ErrorDTO(String message) {
		this.message = message;
	}
	public ErrorDTO() {
	}
}
