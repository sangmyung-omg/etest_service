package com.tmax.eTest.TestStudio.dto.problems.base;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseTestProblemDTO {
	//Long
	private String probID;
	//
	private String partID;
	private String subject;
	private String status;
	
}
